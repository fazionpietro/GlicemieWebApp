package it.univr.glicemiewebapp.service;

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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComunicazioneService {
  private final ComunicazioneRepository comunicazioneRepository;
  private final PazienteRepository pazienteRepository;

  private final ApplicationEventPublisher publisher;

  @Transactional
  public Comunicazione salvaComunicazione(Comunicazione comunicazione, UUID idPaziente) {
    try {
      Paziente paziente = pazienteRepository.findById(idPaziente)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paziente non trovato"));
      comunicazione.setIdPaziente(paziente);

      ComunicazioneMedicoDTO dto = new ComunicazioneMedicoDTO(paziente.getId(),
          comunicazione.getPriorita(),
          comunicazione.getDescrizione(),
          paziente.getNome(),
          paziente.getCognome(),
          paziente.getEmail(),
          comunicazione.getTimestamp());

      publisher.publishEvent(new NewComunicazioneEvent(paziente.getIdMedico(), dto));

      return comunicazioneRepository.save(comunicazione);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "errore: ");
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
}
