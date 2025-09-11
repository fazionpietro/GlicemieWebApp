package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.UtenteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository repository;

    @Autowired
    private ObjectMapper mapper; 

    
    public UtenteService(UtenteRepository repository) {
        this.repository = repository;
    }


    public List<Utente> getAll() {
        return repository.findAll();
    }

    public Optional<Utente> getById(UUID id) {
        return repository.findById(id);
    }


    public Utente create(Utente u) {
        return repository.save(u);
    }

    public Utente update(Utente u) {
        return repository.save(u);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public Optional<Utente> getByEmail(String email) {
        return repository.findByEmailAddress(email);
    }



    public ResponseEntity<String> getMedici(){
        
        try {
            return new ResponseEntity<>(mapper.writeValueAsString(repository.findByRole("ROLE_MEDICO")), HttpStatus.OK);
            
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED TO RETRIVE DATA");
        }


    }


}
