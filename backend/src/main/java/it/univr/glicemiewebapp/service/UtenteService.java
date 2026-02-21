package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.dto.UtenteDTO;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import it.univr.glicemiewebapp.exception.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing generic User entities.
 * Handles CRUD operations for users and retrieval by role.
 */
@Service
@RequiredArgsConstructor
public class UtenteService {

  private final PasswordEncoder passwordEncoder;
  private final UtenteRepository repository;
  private final LogService logger;

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
        .orElseThrow(() -> new BusinessException("DATA_RETRIEVAL_ERROR", "User not found"));

    logger.warn("Modifying data of user: " + utente.toString());

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

      if (request.getPasswordHash() != null && !request.getPasswordHash().trim().isEmpty()) {
        utente.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
      }

      return new ResponseEntity<>("USER UPDATED", HttpStatus.OK);

    } catch (Exception e) {
      if (e instanceof ValidationException) {
        throw e;
      }
      throw new BusinessException("UPDATE_ERROR", "Failed to update user");
    }
  }

}
