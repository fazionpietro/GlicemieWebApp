package it.univr.glicemiewebapp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.univr.glicemiewebapp.dto.PazienteDTO;
import it.univr.glicemiewebapp.dto.PazienteUtenteDTO;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import it.univr.glicemiewebapp.exception.*;

@Service
public class PazienteService {

  private final PasswordEncoder passwordEncoder;
  private final PazienteRepository pazienteRepository;
  private final UtenteRepository utenteRepository;
  private final LogService logger;

  public PazienteService(PasswordEncoder passwordEncoder,
      PazienteRepository pazienteRepository,
      UtenteRepository utenteRepository,
      LogService logger) {
    this.passwordEncoder = passwordEncoder;
    this.pazienteRepository = pazienteRepository;
    this.utenteRepository = utenteRepository;
    this.logger = logger;
  }

  public List<PazienteUtenteDTO> findAllComplete() {
    try {
      return pazienteRepository.findAllComplete();

    } catch (Exception e) {
      throw new BusinessException("DATA_RETRIEVAL_ERROR", "Failed to retrieve patients data");
    }
  }

  public List<PazienteDTO> findByMedico(UUID id) {
    try {
      Utente medico = utenteRepository.findById(id).get();

      return pazienteRepository.findByIdMedico(medico);

    } catch (Exception e) {
      throw new BusinessException("DATA_RETRIEVAL_ERROR", "Failed to retrieve patients for medico");
    }
  }

  @Transactional
  public ResponseEntity<String> update(PazienteUtenteDTO request) {

    Paziente paziente = pazienteRepository.findById(request.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DATABASE ERROR"));

    logger.warn("attempt to modify user: " + paziente.toString());
    try {

      Optional.ofNullable(request.getEmail()).ifPresent(paziente::setEmail);
      Optional.ofNullable(request.getNome()).ifPresent(paziente::setNome);
      Optional.ofNullable(request.getCognome()).ifPresent(paziente::setCognome);
      Optional.ofNullable(request.getDataNascita()).ifPresent(paziente::setDataNascita);
      Optional.ofNullable(request.getFattoriRischio()).ifPresent(paziente::setFattoriRischio);
      Optional.ofNullable(request.getComorbita()).ifPresent(paziente::setComorbita);
      Optional.ofNullable(request.getPatologiePregresse()).ifPresent(paziente::setPatologiePregresse);

      if (request.getIdMedico() != null) {
        Utente medico = utenteRepository.findById(request.getIdMedico())
            .orElseThrow(() -> new ResourceNotFoundException("Medico", request.getIdMedico()));
        paziente.setIdMedico(medico);
      }

      if (request.getPasswordHash() != null)
        paziente.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));

      pazienteRepository.save(paziente);

      return new ResponseEntity<>("USER UPDATED", HttpStatus.OK);

    } catch (Exception e) {
      throw new BusinessException("DATA_RETRIEVAL_ERROR", "Failed to retrieve patients for medico");
    }

  }

}
