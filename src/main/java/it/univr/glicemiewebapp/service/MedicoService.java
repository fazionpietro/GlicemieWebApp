package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.entity.Medico;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MedicoService {
    private MedicoRepository repository;

    @Autowired
    public MedicoService(MedicoRepository repository) {
        this.repository = repository;
    }

    public List<Medico> getAll() {
        return repository.findAll();
    }

    public Optional<Medico> getById(UUID id) {
        return repository.findById(id);
    }


    public Medico create(Medico u) {
        return repository.save(u);
    }

    public Medico update(Medico u) {
        return repository.save(u);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }


    public String getRole(Utente u) {
        return "ROLE_MEDICO";
    }
}
