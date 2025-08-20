package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UtenteService {

    private UtenteRepository repository;

    @Autowired
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

}
