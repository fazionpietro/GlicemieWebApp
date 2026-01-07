package it.univr.glicemiewebapp.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.univr.glicemiewebapp.dto.response.AuthenticationResponse;
import it.univr.glicemiewebapp.dto.response.MessageResponse;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.forms.AdminForm;
import it.univr.glicemiewebapp.forms.MedicoForm;
import it.univr.glicemiewebapp.forms.PazienteForm;
import it.univr.glicemiewebapp.forms.SignInForm;
import it.univr.glicemiewebapp.forms.UtenteForm;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

  @Value("${security.jwt.expiration-time}")
  private long expirationTimeMs;

  private final UtenteRepository utenteRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final LogService logger;

  public ResponseEntity<AuthenticationResponse> register(UtenteForm req) {
    logger.info("Tentativo di registrazione per email: " + req.getEmail());

    // Verifica se l'email esiste già
    if (utenteRepository.findByEmailAddress(req.getEmail()).isPresent()) {
        logger.warn("Tentativo di registrazione con email già esistente: " + req.getEmail());
        throw new ResponseStatusException(HttpStatus.CONFLICT, "EMAIL EXIST");
    }

    if (req instanceof PazienteForm pazienteForm) {
        return handlePazienteRegistration(pazienteForm);
    } else if (req instanceof AdminForm adminForm) {
        return handleAdminRegistration(adminForm);
    } else if (req instanceof MedicoForm medicoForm) {
        return handleMedicoRegistration(medicoForm);
    } else {
        logger.error("Tipo di form non riconosciuto: " + req.getClass().getSimpleName());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UNSUPPORTED FORM TYPE");
    }
  }

  private ResponseEntity<AuthenticationResponse> handlePazienteRegistration(PazienteForm pazienteForm) {
    logger.info("Registrazione paziente per email: " + pazienteForm.getEmail());

    Paziente newPaziente = new Paziente(
        pazienteForm.getEmail(),
        passwordEncoder.encode(pazienteForm.getPassword()),
        pazienteForm.getNome(),
        pazienteForm.getCognome(),
        pazienteForm.getDataNascita(),
        pazienteForm.getFattoriRischio(),
        pazienteForm.getComorbita(),
        pazienteForm.getPatologiePregresse());

    if (pazienteForm.getIdMedico() != null)
        newPaziente.setIdMedico(utenteRepository.findById(pazienteForm.getIdMedico())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "MEDICO NOT FOUND")));

    utenteRepository.save(newPaziente);
    logger.info("Paziente registrato con successo: " + newPaziente.getId().toString());

    return createSuccessResponse(newPaziente, newPaziente.getId().toString());
  }

  private ResponseEntity<AuthenticationResponse> handleAdminRegistration(AdminForm adminForm) {
    logger.info("Registrazione admin per email: " + adminForm.getEmail());

    Utente newAdmin = new Utente(null,
        adminForm.getEmail(),
        passwordEncoder.encode(adminForm.getPassword()),
        adminForm.getNome(),
        adminForm.getCognome(),
        adminForm.getDataNascita(),
        adminForm.getRuolo());

    utenteRepository.save(newAdmin);
    logger.info("Admin registrato con successo: " + adminForm.getEmail());

    return createSuccessResponse(newAdmin, newAdmin.getId().toString());
  }

  private ResponseEntity<AuthenticationResponse> handleMedicoRegistration(MedicoForm medicoForm) {
    logger.info("Registrazione medico per email: " + medicoForm.getEmail());

    Utente newMedico = new Utente(null,
        medicoForm.getEmail(),
        passwordEncoder.encode(medicoForm.getPassword()),
        medicoForm.getNome(),
        medicoForm.getCognome(),
        medicoForm.getDataNascita(),
        medicoForm.getRuolo());

    utenteRepository.save(newMedico);
    logger.info("Medico registrato con successo: " + medicoForm.getEmail());

    return createSuccessResponse(newMedico, newMedico.getId().toString());
  }

  private ResponseEntity<AuthenticationResponse> createSuccessResponse(Utente utente, String id) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + jwtService.generateToken(utente));

    AuthenticationResponse body = AuthenticationResponse.builder()
            .message("SUCCESS TO CREATE USER")
            .id(id)
            .build();

    return new ResponseEntity<>(body, headers, HttpStatus.CREATED);
  }

  public ResponseEntity<AuthenticationResponse> authentication(SignInForm signInForm) {
    logger.info("Tentativo di autenticazione per email: " + signInForm.getEmail());

    Utente user = utenteRepository.findByEmailAddress(signInForm.getEmail())
            .orElseThrow(() -> {
                logger.warn("Tentativo di login con email non esistente: " + signInForm.getEmail());
                return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INCORRECT EMAIL");
            });

    if (!passwordEncoder.matches(signInForm.getPassword(), user.getPasswordHash())) {
        logger.warn("Tentativo di login con password errata per email: " + signInForm.getEmail());
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INCORRECT PASSWORD");
    }

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(signInForm.getEmail(), signInForm.getPassword()));

    UUID id = user.getId();
    String token = jwtService.generateToken(user);

    ResponseCookie cookie = ResponseCookie.from("token", token)
        .httpOnly(true)
        .secure(false)
        .sameSite("Strict")
        .path("/")
        .maxAge(expirationTimeMs)
        .build();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Set-Cookie", cookie.toString());

    AuthenticationResponse body = AuthenticationResponse.builder()
            .message("CREDENTIAL VALIDATED")
            .id(id.toString())
            .role(user.getRuolo())
            .build();

    logger.info("Autenticazione completata con successo per email: " + signInForm.getEmail());
    return new ResponseEntity<>(body, headers, HttpStatus.ACCEPTED);
  }

  public Boolean validateToken(String token) {
    try {
      return jwtService.checkValidity(token);
    } catch (Exception e) {
      logger.error("Errore durante la validazione del token: " + e.getMessage());
      return false;
    }
  }

  public ResponseEntity<MessageResponse> logout(String token) {
    if (!jwtService.checkValidity(token)) {
      return new ResponseEntity<>(new MessageResponse("INVALID TOKEN"), HttpStatus.UNAUTHORIZED);
    }

    jwtService.addToBlacklist(token);

    logger.info("Logout successful for token: " + (token.length() > 10 ? token.substring(0, 10) : token) + "...");

    return new ResponseEntity<>(new MessageResponse("LOGOUT SUCCESSFUL"), HttpStatus.OK);
  }
}
