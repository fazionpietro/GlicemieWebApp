package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.forms.AdminForm;
import it.univr.glicemiewebapp.forms.MedicoForm;
import it.univr.glicemiewebapp.forms.PazienteForm;
import it.univr.glicemiewebapp.forms.SignInForm;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import it.univr.glicemiewebapp.service.AuthenticationService;
import it.univr.glicemiewebapp.service.JwtService;
import it.univr.glicemiewebapp.service.LogService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for authentication: login, registration, token verification, and
 * logout.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;
    private final JwtService jwtService;
    private final LogService logger;

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody @Valid SignInForm signInForm) {
        logger.info("Login attempt");
        try {
            return authenticationService.authentication(signInForm);
        } catch (ResponseStatusException e) {
            log.warn("Login fallito: {}", e.getReason());
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PostMapping("/signup/medico")
    public ResponseEntity<String> registerMedico(@RequestBody @Valid MedicoForm medico) {
        logger.info("Attempt to create a new doctor");
        try {
            return authenticationService.register(medico);
        } catch (ResponseStatusException e) {
            log.warn("Registrazione medico fallita: {}", e.getReason());
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid AdminForm admin) {
        logger.info("Attempt to create a new admin");
        try {
            return authenticationService.register(admin);
        } catch (ResponseStatusException e) {
            log.warn("Registrazione admin fallita: {}", e.getReason());
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PostMapping("/signup/paziente")
    public ResponseEntity<String> registerPaziente(@RequestBody @Valid PazienteForm paziente) {
        logger.info("Attempt to create a new patient");
        try {
            return authenticationService.register(paziente);
        } catch (ResponseStatusException e) {
            log.warn("Registrazione paziente fallita: {}", e.getReason());
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyToken(HttpServletRequest request) {
        try {
            String token = extractTokenFromCookie(request);
            if (token == null || !jwtService.checkValidity(token)) {
                return new ResponseEntity<>("INVALID TOKEN", HttpStatus.UNAUTHORIZED);
            }

            String email = jwtService.getMailFromToken(token);
            Optional<Utente> userOpt = utenteRepository.findByEmailAddress(email);
            if (userOpt.isEmpty()) {
                return new ResponseEntity<>("USER NOT FOUND", HttpStatus.UNAUTHORIZED);
            }

            Utente user = userOpt.get();
            JSONObject response = new JSONObject();
            response.put("id", user.getId().toString());
            response.put("email", user.getEmail());
            response.put("role", user.getRuolo());
            response.put("message", "TOKEN VALID");

            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Errore durante la verifica del token: {}", e.getMessage(), e);
            return new ResponseEntity<>("TOKEN VERIFICATION FAILED", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = extractTokenFromCookie(request);
            logger.warn("Logout – token estratto");
            authenticationService.logout(token);

            // Invalida il cookie impostando maxAge a 0
            ResponseCookie cookie = ResponseCookie.from("token", "")
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(0)
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());

            JSONObject body = new JSONObject();
            body.put("message", "LOGOUT SUCCESSFUL");
            return new ResponseEntity<>(body.toString(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Errore durante il logout: {}", e.getMessage(), e);
            return new ResponseEntity<>("LOGOUT FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** Extracts JWT from the "token" cookie in the request. */
    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
