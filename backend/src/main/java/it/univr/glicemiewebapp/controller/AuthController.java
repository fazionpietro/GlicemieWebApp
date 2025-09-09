package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.forms.AdminForm;
import it.univr.glicemiewebapp.forms.MedicoForm;
import it.univr.glicemiewebapp.forms.PazienteForm;
import it.univr.glicemiewebapp.forms.SignInForm;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import it.univr.glicemiewebapp.service.AuthenticationService;
import it.univr.glicemiewebapp.service.JwtService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;



@Slf4j
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

    @CrossOrigin    
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
    public ResponseEntity<String> registerMedico(@RequestBody @Valid MedicoForm medico) {
        log.error(medico.toString());
        try{

            return authenticationService.register(medico);
        }catch(ResponseStatusException e){
            System.out.println(e);
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid AdminForm admin) {
        try{
            return authenticationService.register(admin);
        }catch(ResponseStatusException e){
            System.out.println(e);
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }

    @PostMapping("/signup/paziente")
    public ResponseEntity<String> registerPaziente(@RequestBody @Valid PazienteForm paziente) {
        try{
            return authenticationService.register(paziente);
        }catch(ResponseStatusException e){
            System.out.println(e);
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }




}
