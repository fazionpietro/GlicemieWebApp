package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.entity.Rilevazione;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.event.NewComunicazioneEvent;
import it.univr.glicemiewebapp.entity.Comunicazione;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.repository.RilevazioneRepository;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import lombok.AllArgsConstructor;
import it.univr.glicemiewebapp.repository.ComunicazioneRepository;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RilevazioneService {
  @Autowired
  private final LogService log;
  @Autowired
  private final RilevazioneRepository rilevazioni;
  @Autowired
  private final PazienteRepository pazienti;
  @Autowired
  private final UtenteRepository utenti;
  @Autowired
  private ObjectMapper mapper;
  @Autowired
  private ComunicazioneRepository comunicazioneRepository;
  @Autowired
  private final ApplicationEventPublisher publisher;

  public Rilevazione addRilevazione(UUID idPaziente, Double valore) {
    Paziente paziente = pazienti.findById(idPaziente)
        .orElseThrow(() -> new IllegalArgumentException("Paziente non trovato"));

    Instant tsp = Instant.now();
    Rilevazione rilevazione = Rilevazione.builder()
        .idPaziente(paziente)
        .valore(valore)
        .timestamp(tsp)
        .build();

    log.info("Paziente id: " + paziente.getId() + "hai inserito una rilevazione" + valore);
    rilevazioni.save(rilevazione);

    if (valore > 180 || valore < 70) {

      Comunicazione c = Comunicazione.builder()
          .priorita(2)
          .idPaziente(paziente)
          .descrizione(paziente.getNominativo()
              + " ha inserito una rilevazione di glicemia fuori dal normale range (valore: " + valore + ")")
          .timestamp(tsp)
          .build();
      comunicazioneRepository.save(c);
      ComunicazioneMedicoDTO cm = ComunicazioneMedicoDTO.builder()
          .id(c.getId())
          .priorita(c.getPriorita())
          .descrizione(c.getDescrizione())
          .nome(paziente.getNome())
          .cognome(paziente.getCognome())
          .email(paziente.getEmail())
          .timestamp(tsp)
          .build();

      publisher.publishEvent(new NewComunicazioneEvent(paziente.getIdMedico(), cm));

    }

    return rilevazione;
  }

  public List<Rilevazione> getAllRilevazioni() {
    return rilevazioni.findAll();
  }

  public List<Rilevazione> getByPaziente(UUID idPaziente) {
    return rilevazioni.findByIdPaziente_Id(idPaziente);
  }

  @Transactional
  public void deleteRilevazione(UUID idRilevazione) {
    rilevazioni.deleteById(idRilevazione);
  }

  public List<RilevazioneUtenteDTO> getByPazienteDTO(UUID idPaziente) {
    return rilevazioni.findByIdPaziente_Id(idPaziente)
        .stream()
        .map(RilevazioneUtenteDTO::new)
        .collect(Collectors.toList());
  }

  public long getRilevazioniOfDay() {

    return rilevazioni.findAll().stream()
        .filter((i) -> !i.getTimestamp().isBefore(Instant.now().minus(24, ChronoUnit.HOURS))).count();
  }

  public ResponseEntity<String> getAllMyRilevazioni(UUID id) {
    Utente u = utenti.findById(id).orElseThrow(() -> new IllegalArgumentException("Paziente non trovato"));
    try {

      if (u.getRuolo().equals("ROLE_ADMIN")) {
        return new ResponseEntity<>(mapper.writeValueAsString(rilevazioni.findAll()), HttpStatus.OK);
      } else if (u.getRuolo().equals("ROLE_MEDICO")) {

        return new ResponseEntity<>(mapper.writeValueAsString(rilevazioni.findAllByPazienteIdMedicoId(id)),
            HttpStatus.OK);
      }
      return new ResponseEntity<>("PERMISSION ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      log.error(e.getMessage());

      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

  }

}
