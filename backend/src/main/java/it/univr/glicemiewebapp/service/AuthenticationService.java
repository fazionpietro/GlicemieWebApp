package it.univr.glicemiewebapp.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
   

    public ResponseEntity<String> register(UtenteForm req) throws ResponseStatusException {
        log.info("Tentativo di registrazione per email: {}", req.getEmail());

        try {
            // Verifica se l'email esiste già
            if (utenteRepository.findByEmailAddress(req.getEmail()).isPresent()) {
                log.warn("Tentativo di registrazione con email già esistente: {}", req.getEmail());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "EMAIL EXIST");
            }

            if (req instanceof PazienteForm pazienteForm) {
                return handlePazienteRegistration(pazienteForm);
            } else if (req instanceof AdminForm adminForm) {
                return handleAdminRegistration(adminForm);
            } else if (req instanceof MedicoForm medicoForm) {
                return handleMedicoRegistration(medicoForm);
            } else {
                log.error("Tipo di form non riconosciuto: {}", req.getClass().getSimpleName());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UNSUPPORTED FORM TYPE");
            }

        } catch (ResponseStatusException e) {
            log.error("Errore durante la registrazione per email {}: {}", req.getEmail(), e.getReason(), e);
            throw e;
        } catch (Exception e) {
            log.error("Errore imprevisto durante la registrazione per email {}: {}", req.getEmail(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "AN ERROR OCCURRED");
        }
    }

    private ResponseEntity<String> handlePazienteRegistration(PazienteForm pazienteForm) {
        log.info("Registrazione paziente per email: {}", pazienteForm.getEmail());

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

            utenteRepository.save(newPaziente);
            log.info("Paziente registrato con successo: {}", newPaziente.getId().toString());

            return createSuccessResponse(newPaziente, newPaziente.getId().toString(), newPaziente.getRuolo());

        } catch (Exception e) {
            log.error("Errore durante il salvataggio del paziente {}: {}", pazienteForm.getEmail(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED TO REGISTER PAZIENTE");
        }
    }

    private ResponseEntity<String> handleAdminRegistration(AdminForm adminForm) {
        log.info("Registrazione admin per email: {}", adminForm.getEmail());

        try {
            Utente newAdmin = new Utente(null,
                    adminForm.getEmail(),
                    passwordEncoder.encode(adminForm.getPassword()),
                    adminForm.getNome(),
                    adminForm.getCognome(),
                    adminForm.getDataNascita(),
                    adminForm.getRuolo());

            utenteRepository.save(newAdmin);
            log.info("Admin registrato con successo: {}", adminForm.getEmail());

            return createSuccessResponse(newAdmin, newAdmin.getId().toString(), newAdmin.getRuolo());

        } catch (Exception e) {
            log.error("Errore durante il salvataggio dell'admin {}: {}", adminForm.getEmail(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED TO REGISTER ADMIN");
        }
    }

    private ResponseEntity<String> handleMedicoRegistration(MedicoForm medicoForm) {
        log.info("Registrazione medico per email: {}", medicoForm.getEmail());

        try {
            Utente newMedico = new Utente(null,
                    medicoForm.getEmail(),
                    passwordEncoder.encode(medicoForm.getPassword()),
                    medicoForm.getNome(),
                    medicoForm.getCognome(),
                    medicoForm.getDataNascita(),
                    medicoForm.getRuolo());

            utenteRepository.save(newMedico);
            log.info("Medico registrato con successo: {}", medicoForm.getEmail());

            return createSuccessResponse(newMedico, newMedico.getId().toString(), newMedico.getRuolo());

        } catch (Exception e) {
            log.error("Errore durante il salvataggio del medico {}: {}", medicoForm.getEmail(), e.getMessage(), e);
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
        log.info("Tentativo di autenticazione per email: {}", signInForm.getEmail());

        try {
            Optional<Utente> userOpt = utenteRepository.findByEmailAddress(signInForm.getEmail());

            if (userOpt.isEmpty()) {
                log.warn("Tentativo di login con email non esistente: {}", signInForm.getEmail());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INCORRECT EMAIL");
            }

            Utente user = userOpt.get();

            if (!passwordEncoder.matches(signInForm.getPassword(), user.getPasswordHash())) {
                log.warn("Tentativo di login con password errata per email: {}", signInForm.getEmail());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INCORRECT PASSWORD");
            }

            // Autenticazione con Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInForm.getEmail(), signInForm.getPassword()));

            UUID id = userOpt.get().getId();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwtService.generateToken(user));

            JSONObject body = new JSONObject();
            body.put("message", "CREDENTIAL VALIDATED");
            body.put("id", id.toString());
            body.put("role", userOpt.get().getRuolo());

            log.info("Autenticazione completata con successo per email: {}", signInForm.getEmail());
            return new ResponseEntity<>(body.toString(), headers, HttpStatus.ACCEPTED);

        } catch (ResponseStatusException e) {
            log.error("Errore durante l'autenticazione per email {}: {}", signInForm.getEmail(), e.getReason(), e);
            throw e;
        } catch (Exception e) {
            log.error("Errore imprevisto durante l'autenticazione per email {}: {}", signInForm.getEmail(),
                    e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "AUTHENTICATION ERROR");
        }
    }

    public Boolean validateToken(String token) {
        log.debug("Validazione token richiesta");

        try {
            boolean isValid = jwtService.checkValidity(token);
            log.debug("Risultato validazione token: {}", isValid);
            return isValid;

        } catch (Exception e) {
            log.error("Errore durante la validazione del token: {}", e.getMessage(), e);
            return false;
        }
    }

   

    public ResponseEntity<String> logout(String token) {
        log.debug("logout di " + token);
        try {

            if (!jwtService.checkValidity(token)) {
                return new ResponseEntity<>("INVALID TOKEN", HttpStatus.UNAUTHORIZED);
            }

            
            jwtService.addToBlacklist(token);

            log.info("Logout successful for token: {}", token.substring(0, 10) + "...");

            JSONObject response = new JSONObject();
            response.put("message", "LOGOUT SUCCESSFUL");

            return new ResponseEntity<>(response.toString(), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Errore durante il logout: {}", e.getMessage(), e);
            return new ResponseEntity<>("LOGOUT ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
            
        }

    }
}