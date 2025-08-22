package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.entity.Admin;
import it.univr.glicemiewebapp.entity.Medico;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.forms.MedicoAdminForm;
import it.univr.glicemiewebapp.forms.PazienteForm;
import it.univr.glicemiewebapp.forms.SignInForm;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import it.univr.glicemiewebapp.service.JwtService;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UtenteRepository utenteRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody @Valid SignInForm signInForm) {
        Optional<Utente> tmp = utenteRepository.findByEmailAddress(signInForm.getEmail());
        JSONObject body = new JSONObject();
        body.put("message", "credenziali errate");


        if(tmp.isPresent()) {
            System.out.println(passwordEncoder.encode(signInForm.getPassword()));
            System.out.println(tmp.get().getPasswordHash());
            System.out.println(passwordEncoder.matches(tmp.get().getPasswordHash(), passwordEncoder.encode(signInForm.getPassword())));




            if(passwordEncoder.matches(signInForm.getPassword(),tmp.get().getPasswordHash())) {

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization",  "Bearer " + jwtService.generateToken(signInForm.getEmail()));

                body.put("message", "credenziali corrette");
                body.put("email", signInForm.getEmail());

                return new ResponseEntity<>(body.toString(),headers, HttpStatus.ACCEPTED);
            }else{


                return new ResponseEntity<>(body.toString(), HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(body.toString(), HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/signup/medico")
    public ResponseEntity<String> registerMedico(@RequestBody @Valid MedicoAdminForm utente) {
        if(utenteRepository.findByEmailAddress(utente.getEmail()).isPresent()) {
            return new ResponseEntity<>("email already exists", HttpStatus.CONFLICT);
        }
        utenteRepository.save(new Medico(utente.getEmail(), passwordEncoder.encode(utente.getPassword()), utente.getNome(), utente.getCognome()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",  "Bearer " + jwtService.generateToken(utente.getEmail()));

        JSONObject body = new JSONObject();
        body.put("message", "medico registrato correttamente");
        body.put("email", utente.getEmail());

        return new ResponseEntity<>(body.toString(),headers, HttpStatus.CREATED);
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid MedicoAdminForm utente) {
        if(utenteRepository.findByEmailAddress(utente.getEmail()).isPresent()) {
            return new ResponseEntity<>("email already exists", HttpStatus.CONFLICT);
        }
        utenteRepository.save(new Admin(utente.getEmail(), passwordEncoder.encode(utente.getPassword()), utente.getNome(), utente.getCognome()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",  "Bearer " + jwtService.generateToken(utente.getEmail()));

        JSONObject body = new JSONObject();
        body.put("message", "admin registrato correttamente");
        body.put("email", utente.getEmail());


        return new ResponseEntity<>(body.toString(),headers, HttpStatus.CREATED);
    }

    @PostMapping("/signup/paziente")
    public ResponseEntity<String> registerPaziente(@RequestBody @Valid PazienteForm utente) {
        if(utenteRepository.findByEmailAddress(utente.getEmail()).isPresent()) {
            return new ResponseEntity<>("email already exists", HttpStatus.CONFLICT);
        }
        utenteRepository.save(new Paziente(
                utente.getEmail(),
                passwordEncoder.encode(utente.getPassword()),
                utente.getNome(),
                utente.getCognome(),
                utente.getDataNascita(),
                utente.getFattoriRischio(),
                utente.getComorbita(),
                utente.getPatologiePregresse()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",  "Bearer " + jwtService.generateToken(utente.getEmail()));

        JSONObject body = new JSONObject();
        body.put("message", "paziente registrato correttamente");
        body.put("email", utente.getEmail());

        return new ResponseEntity<>(body.toString(),headers, HttpStatus.CREATED);
    }




}
