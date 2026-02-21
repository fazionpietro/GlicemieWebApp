package it.univr.glicemiewebapp.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.univr.glicemiewebapp.dto.AlertTerapia;
import it.univr.glicemiewebapp.dto.ComunicazioneMedicoDTO;
import it.univr.glicemiewebapp.dto.TerapiaDTO;
import it.univr.glicemiewebapp.entity.Terapia;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.event.NewComunicazioneEvent;
import it.univr.glicemiewebapp.entity.Comunicazione;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.exception.BusinessException;
import it.univr.glicemiewebapp.repository.ComunicazioneRepository;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.repository.TerapiaRepository;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing medical therapies.
 * Handles creation, updates, deletion, and monitoring of therapy adherence.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TerapiaService {

  private final TerapiaRepository terapiaRepository;
  private final UtenteRepository utenteRepository;
  private final PazienteRepository pazienteRepository;
  private final ApplicationEventPublisher publisher;
  private final ComunicazioneRepository comunicazioneRepository;

  public ResponseEntity<String> create(TerapiaDTO t) {
    try {
      Terapia newTerapia = new Terapia(null, t.getFarmaco(), t.getNumAssunzioni(), t.getDosaggio(), t.getIndicazioni(),
          pazienteRepository.findById(t.getIdPaziente()).get(), utenteRepository.findById(t.getIdMedico()).get());

      terapiaRepository.save(newTerapia);

      return new ResponseEntity<>("TERAPIA CREATA", HttpStatus.OK);
    } catch (Exception e) {
      throw new BusinessException("CREATION_ERROR", "failed to create terapy");
    }
  }

  public List<TerapiaDTO> getAllByMedico(UUID id) {
    try {
      Utente medico = utenteRepository.findById(id).get();

      return terapiaRepository.findByMedicoCurante(medico);
    } catch (Exception e) {
      throw new BusinessException("DATA_RETRIEVAL_ERROR", "Failed to retrieve medic data");
    }
  }

  public ResponseEntity<String> delete(UUID id) {
    try {

      terapiaRepository.deleteById(id);

      return new ResponseEntity<>("TERAPIA ELIMINATA", HttpStatus.OK);
    } catch (Exception e) {
      throw new BusinessException("DELETION_ERROR", "Failed to delete therapy");
    }
  }

  @Transactional
  public ResponseEntity<String> update(TerapiaDTO t, UUID id) {
    try {
      Terapia existingTerapia = terapiaRepository.findById(id)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DATABASE ERROR"));

      // Aggiorna i campi modificabili
      existingTerapia.setFarmaco(t.getFarmaco());
      existingTerapia.setNumAssunzioni(t.getNumAssunzioni());
      existingTerapia.setDosaggio(t.getDosaggio());
      existingTerapia.setIndicazioni(t.getIndicazioni());

      terapiaRepository.save(existingTerapia);
      return new ResponseEntity<>("TERAPIA AGGIORNATA", HttpStatus.OK);
    } catch (Exception e) {
      throw new BusinessException("UPDATE_ERROR", "failed to update therapy");
    }
  }

  @Scheduled(cron = "0 0 */6 * * *", zone = "Europe/Berlin")
  @EventListener(ApplicationReadyEvent.class)
  public void checkForAlert() {

    try {
      Instant threeDaysAgo = Instant.now().minus(Duration.ofDays(3));
      List<AlertTerapia> list = terapiaRepository.findPazientiInadempienti(threeDaysAgo);
      Comunicazione c;
      ComunicazioneMedicoDTO cm;
      Paziente p;
      Terapia t;
      Instant tsp;
      String description;

      for (AlertTerapia a : list) {
        log.info(a.toString());
        p = pazienteRepository.findById(a.getIdPaziente()).get();
        t = terapiaRepository.findById(a.getIdTerapia()).get();
        tsp = Instant.now();
        description = "Paziente " + p.getNominativo() + " non assume " + t.getFarmaco() + " da almeno 3 giorni";

        c = new Comunicazione(null,
            3,
            p,
            description,
            false,
            tsp);
        comunicazioneRepository.save(c);
        cm = new ComunicazioneMedicoDTO(c.getId(),
            3,
            description,
            p.getNome(),
            p.getCognome(),
            p.getEmail(),
            tsp);

        publisher.publishEvent(new NewComunicazioneEvent(p.getIdMedico(), cm));
      }

    } catch (Exception e) {
      throw new BusinessException("DATA_RETRIEVAL_ERROR", "Failed to retrieve data");
    }

  }

  public List<TerapiaDTO> getAllByPaziente(UUID idPaziente) {
    try {
      Paziente paziente = pazienteRepository.findById(idPaziente)
          .orElseThrow(() -> new BusinessException("PATIENT_NOT_FOUND", "paziente non trovato"));
      return terapiaRepository.findByPaziente(paziente);
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      throw new BusinessException("DATA_RETRIEVAL_ERROR", "Failed to retrieve patient's therapie");
    }
  }
}
