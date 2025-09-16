package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.dto.UtenteDTO;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.forms.UtenteForm;
import it.univr.glicemiewebapp.repository.RilevazioneRepository;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import it.univr.glicemiewebapp.exception.*;
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

  public List<UtenteDTO> getMedici() {

    try {
      return repository.findByRole("ROLE_MEDICO");

    } catch (Exception e) {
      throw new BusinessException("DATA_RETRIEVAL_ERROR", "Failed to retrieve medic data");
    }
  }

  public ResponseEntity<String> deleteByID(UUID id) {
    try {
      repository.deleteById(id);

      return new ResponseEntity<>("{message: \"UTENTE ELIMINATO\" }", HttpStatus.OK);

    } catch (Exception e) {

      throw new BusinessException("DELETION_ERROR", "Failed to delete user");
    }

  }

  @Transactional
  public ResponseEntity<String> update(UtenteDTO request) {

    Utente utente = repository.findById(request.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DATABASE ERROR"));

    logger.warn("Modifing data of (Terapia): " + utente.toString());

    try {
      Optional.ofNullable(request.getEmail()).ifPresent(email -> {
        if (!email.equals(utente.getEmail()) && repository.existsByEmail(email)) {
          throw new ValidationException("Email already exists");
        }
        utente.setEmail(email);
      });

      Optional.ofNullable(request.getNome()).ifPresent(utente::setNome);
      Optional.ofNullable(request.getCognome()).ifPresent(utente::setCognome);
      Optional.ofNullable(request.getDataNascita()).ifPresent(utente::setDataNascita);
      if (request.getPasswordHash() != null)
        utente.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
      return new ResponseEntity<>("USER UPDATED", HttpStatus.OK);

    } catch (Exception e) {
      throw new BusinessException("DATA_RETRIEVAL_ERROR", "Failed to retrieve patients for medico");
    }

  }

}
