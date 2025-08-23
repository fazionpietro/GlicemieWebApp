package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.forms.MedicoAdminForm;
import it.univr.glicemiewebapp.forms.PazienteForm;
import it.univr.glicemiewebapp.forms.SignInForm;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import it.univr.glicemiewebapp.service.AuthenticationService;
import it.univr.glicemiewebapp.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UtenteRepository utenteRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody @Valid SignInForm signInForm) {
        try{
            return authenticationService.authentication(signInForm);
        }catch(ResponseStatusException e){
            System.out.println(e);
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }



    @PostMapping("/signup/medico")
    public ResponseEntity<String> registerMedico(@RequestBody @Valid MedicoAdminForm utente) {
        try{

            return authenticationService.registerMedico(utente);
        }catch(ResponseStatusException e){
            System.out.println(e);
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid MedicoAdminForm utente) {
        try{
            return authenticationService.registerAdmin(utente);
        }catch(ResponseStatusException e){
            System.out.println(e);
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }

    @PostMapping("/signup/paziente")
    public ResponseEntity<String> registerPaziente(@RequestBody @Valid PazienteForm utente) {
        try{
            return authenticationService.registerPaziente(utente);
        }catch(ResponseStatusException e){
            System.out.println(e);
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }




}
