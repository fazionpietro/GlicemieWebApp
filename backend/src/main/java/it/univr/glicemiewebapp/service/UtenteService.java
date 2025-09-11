package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.dto.UtenteDTO;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UtenteService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UtenteRepository repository;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private LogService logger;

    public UtenteService(UtenteRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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

    public ResponseEntity<String> getMedici() {

        try {
            return new ResponseEntity<>(mapper.writeValueAsString(repository.findByRole("ROLE_MEDICO")), HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED TO RETRIVE DATA");
        }

    }

    public ResponseEntity<String> deleteByID(UUID id) {
        try {
            repository.deleteById(id);

            return new ResponseEntity<>("{message: \"UTENTE ELIMINATO\" }", HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
        }

    }

    @Transactional
    public ResponseEntity<String> update(UtenteDTO up) {

        log.info(up.toString());

        Utente u = repository.findById(up.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DATABASE ERROR"));

        logger.info("Modifing data of: " + u.toString());

        try {
            log.info(up.toString());

            if (up.getEmail() != null)
                u.setEmail(up.getEmail());

            if (up.getPasswordHash() != null)
                u.setPasswordHash(passwordEncoder.encode(up.getPasswordHash()));

            if (up.getNome() != null)
                u.setNome(up.getNome());

            if (up.getCognome() != null)
                u.setCognome(up.getCognome());

            if (up.getDataNascita() != null)
                u.setDataNascita(up.getDataNascita());

            return new ResponseEntity<>("USER UPDATED", HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
