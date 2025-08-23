package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.entity.Admin;
import it.univr.glicemiewebapp.entity.Medico;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.forms.MedicoAdminForm;
import it.univr.glicemiewebapp.forms.PazienteForm;
import it.univr.glicemiewebapp.forms.SignInForm;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<String> registerMedico(MedicoAdminForm userData) throws ResponseStatusException {

        if (utenteRepository.findByEmailAddress(userData.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "EMAIL EXIST");
        }
        Medico med = new Medico(userData.getEmail(), passwordEncoder.encode(userData.getPassword()), userData.getNome(), userData.getCognome());
        try{
            utenteRepository.save(med);
        }catch (ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED TO REGISTER");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",  "Bearer " + jwtService.generateToken(med));
        JSONObject body = new JSONObject();
        body.put("message", "SUCCESS TO CREATE USER");
        body.put("email", userData.getEmail());

        return new ResponseEntity<>(body.toString(),headers, HttpStatus.CREATED);

    }

    public ResponseEntity<String> registerAdmin(MedicoAdminForm userData) throws ResponseStatusException {

        if (utenteRepository.findByEmailAddress(userData.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "EMAIL EXIST");
        }
        Admin admin = new Admin(userData.getEmail(), passwordEncoder.encode(userData.getPassword()), userData.getNome(), userData.getCognome());

        try{
            utenteRepository.save(admin);
        }catch (ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED TO REGISTER");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",  "Bearer " + jwtService.generateToken(admin));
        JSONObject body = new JSONObject();
        body.put("message", "SUCCESS TO CREATE USER");
        body.put("email", userData.getEmail());
        return new ResponseEntity<>(body.toString(),headers, HttpStatus.CREATED);

    }

    public ResponseEntity<String> registerPaziente(PazienteForm userData) throws ResponseStatusException {

        if (utenteRepository.findByEmailAddress(userData.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "EMAIL EXIST");
        }
        Utente paz = new Paziente(
                userData.getEmail(),
                passwordEncoder.encode(userData.getPassword()),
                userData.getNome(),
                userData.getCognome(),
                userData.getDataNascita(),
                userData.getFattoriRischio(),
                userData.getComorbita(),
                userData.getPatologiePregresse());

        try{
            utenteRepository.save(paz);
        }catch (ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED TO REGISTER");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",  "Bearer " + jwtService.generateToken(paz));
        JSONObject body = new JSONObject();
        body.put("message", "SUCCESS TO CREATE USER");
        body.put("email", userData.getEmail());
        return new ResponseEntity<>(body.toString(),headers, HttpStatus.CREATED);

    }

    public ResponseEntity<String> authentication(SignInForm signInForm) throws ResponseStatusException {

        Optional<Utente> tmp = utenteRepository.findByEmailAddress(signInForm.getEmail());

        if(tmp.isPresent()) {

            if(passwordEncoder.matches(signInForm.getPassword(),tmp.get().getPasswordHash())) {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(signInForm.getEmail(), signInForm.getPassword())
                );
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization",  "Bearer " + jwtService.generateToken(tmp.get()));
                JSONObject body = new JSONObject();
                body.put("message", "CREDENTIAL VALIDATED");
                body.put("email", signInForm.getEmail());

                return new ResponseEntity<>(body.toString(),headers, HttpStatus.ACCEPTED);
            }else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INCORRECT PASSWORD");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INCORRECT EMAIL");
    }



    public Boolean validateToken(String token) {
        return jwtService.checkValidity(token);
    }

}
