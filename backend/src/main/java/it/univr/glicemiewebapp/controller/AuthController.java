package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.response.AuthenticationResponse;
import it.univr.glicemiewebapp.dto.response.MessageResponse;
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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LogService logger;

    @CrossOrigin
    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signin(@RequestBody @Valid SignInForm signInForm) {
        logger.info("Login attempt");
        return authenticationService.authentication(signInForm);
    }

    @PostMapping("/signup/medico")
    public ResponseEntity<AuthenticationResponse> registerMedico(@RequestBody @Valid MedicoForm medico) {
        logger.info("Attempt to create a new doctor");
        return authenticationService.register(medico);
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody @Valid AdminForm admin) {
        logger.info("Attempt to create a new admin");
        return authenticationService.register(admin);
    }

    @PostMapping("/signup/paziente")
    public ResponseEntity<AuthenticationResponse> registerPaziente(@RequestBody @Valid PazienteForm paziente) {
        logger.info("Attempt to create a new patient");
        return authenticationService.register(paziente);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(HttpServletRequest request) {
        String token = extractTokenFromCookie(request);
        logger.info("Veryfing Token: " + token);

        if (token == null || !jwtService.checkValidity(token)) {
            return new ResponseEntity<>("INVALID TOKEN", HttpStatus.UNAUTHORIZED);
        }

        String email = jwtService.getMailFromToken(token);
        Optional<Utente> userOpt = utenteRepository.findByEmailAddress(email);

        if (userOpt.isEmpty()) {
            return new ResponseEntity<>("USER NOT FOUND", HttpStatus.UNAUTHORIZED);
        }

        Utente user = userOpt.get();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .id(user.getId().toString())
                .role(user.getRuolo())
                .message("TOKEN VALID")
                // email is not in AuthenticationResponse usually, but we can reuse it or create another DTO if strictly needed to be identical JSON structure.
                // The previous code returned: {id, email, role, message}
                // My AuthenticationResponse has: {message, id, role}
                // I should add email to AuthenticationResponse or create a UserResponse
                .build();

        // Let's modify AuthenticationResponse to include email if needed to be identical visually/functionally.
        // Wait, the previous code returned a JSONObject with "email" property.
        // I should check if the frontend uses "email".
        // In Login.tsx:
        /*
          const user: User = {
            id: response.data.id,
            email: email, // This is from local state in Login.tsx
            role: response.data.role,
          };
        */
        // Login.tsx uses local email variable.
        // But what about other places? The `verify` endpoint is likely used for session restoration.
        // If I look at `frontend/web-interface/src/context/AuthContext.tsx` I might see it.
        // But for now, let's keep it clean. I will create a specific DTO for Verify if needed or just use a Map for now to be safe on this specific endpoint which was returning unstructured JSON.

        // Actually, let's just use a Map or a specific DTO.
        // To be safe and "visually identical" in response, I should match the keys.

        return new ResponseEntity<>(java.util.Map.of(
            "id", user.getId().toString(),
            "email", user.getEmail(),
            "role", user.getRuolo(),
            "message", "TOKEN VALID"
        ), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletRequest request,
                                     HttpServletResponse response) {

        String token = extractTokenFromCookie(request);
        logger.warn("Logout – token estratto: "+ token);

        ResponseEntity<MessageResponse> svcResp = authenticationService.logout(token); // logic is in service now

        // Invalida il cookie impostando maxAge a 0
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false) // Imposta true in produzione con HTTPS
                .sameSite("Strict")
                .path("/")
                .maxAge(0) // Invalida immediatamente
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        log.info("Logout completato con successo");
        return svcResp;
    }

    // Metodo helper per estrarre il token dal cookie
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
