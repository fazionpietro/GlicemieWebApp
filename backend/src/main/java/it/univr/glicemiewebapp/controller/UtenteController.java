package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {
    @Autowired
    private UtenteRepository repository;

    @GetMapping
    public List<Utente> getAllUtenti() {
        return repository.findAll();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Utente> getUtenteById(@PathVariable UUID id) {
        Optional<Utente> result = repository.findById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Utente> getUtenteByEmail(@PathVariable String email) {
        Optional<Utente> result = repository.findByEmailAddress(email);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}