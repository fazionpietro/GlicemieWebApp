package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.dto.ComunicazioneDTO;
import it.univr.glicemiewebapp.dto.ComunicazioneMedicoDTO;
import it.univr.glicemiewebapp.entity.Comunicazione;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.event.NewComunicazioneEvent;
import it.univr.glicemiewebapp.exception.BusinessException;
import it.univr.glicemiewebapp.repository.ComunicazioneRepository;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComunicazioneService {
  private final ComunicazioneRepository comunicazioneRepository;
  private final PazienteRepository pazienteRepository;

  private final ApplicationEventPublisher publisher;

  public Comunicazione salvaComunicazione(ComunicazioneDTO dto) {
    try {
      Comunicazione comunicazione = Comunicazione.builder().priorita(dto.getPriorita())
          .descrizione(dto.getDescrizione()).timestamp(Instant.now()).build();

      Paziente paziente = pazienteRepository.findById(dto.getIdPaziente())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paziente non trovato"));
      comunicazione.setIdPaziente(paziente);
      comunicazioneRepository.save(comunicazione);
      ComunicazioneMedicoDTO medicDto = ComunicazioneMedicoDTO.builder()
          .id(comunicazione.getId())
          .priorita(comunicazione.getPriorita())
          .descrizione(comunicazione.getDescrizione())
          .nome(paziente.getNome())
          .cognome(paziente.getCognome())
          .email(paziente.getEmail())
          .timestamp(comunicazione.getTimestamp()).build();

      publisher.publishEvent(new NewComunicazioneEvent(paziente.getIdMedico(), medicDto));

      return comunicazione;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "errore: " + e.getLocalizedMessage());
    }
  }

  public List<ComunicazioneMedicoDTO> getByMedico(UUID medico) {

    try {
      List<ComunicazioneMedicoDTO> comunicazioni = comunicazioneRepository.findByMedico(medico);
      return comunicazioni;

    } catch (Exception e) {
      throw new BusinessException("DELETION_ERROR", "Failed to delete user");

    }

  }

  @Transactional
  public String markAsRead(UUID id) {

    try {
      Comunicazione c = comunicazioneRepository.findById(id)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comunication not found"));

      c.setLetto(true);
      return "Comunication (" + id + ") marked as read";
    } catch (Exception e) {
      throw new BusinessException("UPDATE_ERROR", "failed to mark comunication as read");
    }
  }
}
