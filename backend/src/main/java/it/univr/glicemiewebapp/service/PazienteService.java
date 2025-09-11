package it.univr.glicemiewebapp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.univr.glicemiewebapp.dto.PazienteUtenteDTO;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PazienteService {

    private final PasswordEncoder passwordEncoder;
    private final PazienteRepository pazienteRepository;
    private final UtenteRepository utenteRepository;
    private final ObjectMapper mapper;

    public PazienteService(PasswordEncoder passwordEncoder,
            PazienteRepository pazienteRepository,
            UtenteRepository utenteRepository,
            ObjectMapper mapper) {
        this.passwordEncoder = passwordEncoder;
        this.pazienteRepository = pazienteRepository;
        this.utenteRepository = utenteRepository;
        this.mapper = mapper;
    }

    public ResponseEntity<String> findAllComplete() {
        try {
            return new ResponseEntity<>(mapper.writeValueAsString(pazienteRepository.findAllComplete()), HttpStatus.OK);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED TO RETRIVE DATA");
        }
    }

    public ResponseEntity<String> deleteByID(UUID id) {
        try {
            pazienteRepository.deleteById(id);

            return new ResponseEntity<>("{message: \"PAZIENTE ELIMINATO\" }", HttpStatus.OK);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");
        }

    }

    public ResponseEntity<String> update(PazienteUtenteDTO up) {

        Paziente p = pazienteRepository.findById(up.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DATABASE ERROR"));

        try {
            log.info(up.toString());

            p.setEmail(up.getEmail());
            p.setNome(up.getNome());
            p.setCognome(up.getCognome());
            p.setDataNascita(up.getDataNascita());
            p.setFattoriRischio(up.getFattoriRischio());
            p.setComorbita(up.getComorbita());
            p.setPatologiePregresse(up.getPatologiePregresse());
            if (up.getIdMedico() != null) {
                p.setIdMedico(utenteRepository.findById(up.getIdMedico())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medico non trovato")));
            } else {
                p.setIdMedico(null);
            }

            if(up.getPasswordHash() != null)
                p.setPasswordHash(passwordEncoder.encode(up.getPasswordHash()));

            pazienteRepository.save(p);

            return new ResponseEntity<>("USER UPDATED", HttpStatus.OK);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR DURING UPDATE");
        }

    }
}
