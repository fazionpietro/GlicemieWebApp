package it.univr.glicemiewebapp.service;

import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

/**
 * Service for authentication and user registration.
 * Handles user sign-up (Patient, Doctor, Admin) and login using JWT.
 */
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

    public ResponseEntity<String> register(UtenteForm req) throws ResponseStatusException {
        logger.info("Tentativo di registrazione per email: " + req.getEmail());

        try {
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

        } catch (ResponseStatusException e) {
            logger.error("Errore durante la registrazione per email " + req.getEmail() + ": " + e.getReason());
            throw e;
        } catch (Exception e) {
            logger.error(
                    "Errore imprevisto durante la registrazione per email " + req.getEmail() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "AN ERROR OCCURRED");
        }
    }

    private ResponseEntity<String> handlePazienteRegistration(PazienteForm pazienteForm) {
        logger.info("Registrazione paziente per email: " + pazienteForm.getEmail());

        try {
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
                newPaziente.setIdMedico(utenteRepository.findById(pazienteForm.getIdMedico()).get());

            utenteRepository.save(newPaziente);
            logger.info("Paziente registrato con successo: " + newPaziente.getId().toString());

            return createSuccessResponse(newPaziente, newPaziente.getId().toString(), newPaziente.getRuolo());

        } catch (Exception e) {
            logger.error(
                    "Errore durante il salvataggio del paziente " + pazienteForm.getEmail() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED TO REGISTER PAZIENTE");
        }
    }

    private ResponseEntity<String> handleAdminRegistration(AdminForm adminForm) {
        logger.info("Registrazione admin per email: " + adminForm.getEmail());

        try {
            Utente newAdmin = new Utente(null,
                    adminForm.getEmail(),
                    passwordEncoder.encode(adminForm.getPassword()),
                    adminForm.getNome(),
                    adminForm.getCognome(),
                    adminForm.getDataNascita(),
                    adminForm.getRuolo());

            utenteRepository.save(newAdmin);
            logger.info("Admin registrato con successo: " + adminForm.getEmail());

            return createSuccessResponse(newAdmin, newAdmin.getId().toString(), newAdmin.getRuolo());

        } catch (Exception e) {
            logger.error("Errore durante il salvataggio dell'admin " + adminForm.getEmail() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED TO REGISTER ADMIN");
        }
    }

    private ResponseEntity<String> handleMedicoRegistration(MedicoForm medicoForm) {
        logger.info("Registrazione medico per email: " + medicoForm.getEmail());

        try {
            Utente newMedico = new Utente(null,
                    medicoForm.getEmail(),
                    passwordEncoder.encode(medicoForm.getPassword()),
                    medicoForm.getNome(),
                    medicoForm.getCognome(),
                    medicoForm.getDataNascita(),
                    medicoForm.getRuolo());

            utenteRepository.save(newMedico);
            logger.info("Medico registrato con successo: " + medicoForm.getEmail());

            return createSuccessResponse(newMedico, newMedico.getId().toString(), newMedico.getRuolo());

        } catch (Exception e) {
            logger.error("Errore durante il salvataggio del medico " + medicoForm.getEmail() + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED TO REGISTER MEDICO");
        }
    }

    private ResponseEntity<String> createSuccessResponse(Utente utente, String id, String role) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtService.generateToken(utente));

        JSONObject body = new JSONObject();
        body.put("message", "SUCCESS TO CREATE USER");
        body.put("id", id);

        return new ResponseEntity<>(body.toString(), headers, HttpStatus.CREATED);
    }

    public ResponseEntity<String> authentication(SignInForm signInForm) throws ResponseStatusException {
        logger.info("Tentativo di autenticazione per email: " + signInForm.getEmail());

        try {
            Optional<Utente> userOpt = utenteRepository.findByEmailAddress(signInForm.getEmail());

            if (userOpt.isEmpty()) {
                logger.warn("Tentativo di login con email non esistente: " + signInForm.getEmail());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INCORRECT EMAIL");
            }

            Utente user = userOpt.get();

            if (!passwordEncoder.matches(signInForm.getPassword(), user.getPasswordHash())) {
                logger.warn("Tentativo di login con password errata per email: " + signInForm.getEmail());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INCORRECT PASSWORD");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInForm.getEmail(), signInForm.getPassword()));

            UUID id = userOpt.get().getId();
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

            JSONObject body = new JSONObject();
            body.put("message", "CREDENTIAL VALIDATED");
            body.put("id", id.toString());
            body.put("role", userOpt.get().getRuolo());

            logger.info("Autenticazione completata con successo per email: " + signInForm.getEmail());
            return new ResponseEntity<>(body.toString(), headers, HttpStatus.ACCEPTED);

        } catch (ResponseStatusException e) {
            logger.error("Errore durante l'autenticazione per email " + signInForm.getEmail() + ": " + e.getReason());
            throw e;
        } catch (Exception e) {
            logger.error("Errore imprevisto durante l'autenticazione per email " + signInForm.getEmail() + ": "
                    + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "AUTHENTICATION ERROR");
        }
    }

    public ResponseEntity<String> logout(String token) {

        try {

            if (!jwtService.checkValidity(token)) {
                return new ResponseEntity<>("INVALID TOKEN", HttpStatus.UNAUTHORIZED);
            }

            jwtService.addToBlacklist(token);

            logger.info("Logout successful for token: " + token.substring(0, 10) + "...");

            JSONObject response = new JSONObject();
            response.put("message", "LOGOUT SUCCESSFUL");

            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Errore durante il logout: " + e.getMessage());
            return new ResponseEntity<>("LOGOUT ERROR", HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}
