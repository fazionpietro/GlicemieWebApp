package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.entity.Rilevazione;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.repository.RilevazioneRepository;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.dto.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class RilevazioneService {
  @Autowired
  private LogService log;
  private final RilevazioneRepository rilevazioni;
  private final PazienteRepository pazienti;
  private final UtenteRepository utenti;
  private ObjectMapper mapper;

  public RilevazioneService(RilevazioneRepository rilevazioni, PazienteRepository pazienti, UtenteRepository utenti,
      ObjectMapper mapper) {
    this.rilevazioni = rilevazioni;
    this.pazienti = pazienti;
    this.utenti = utenti;
    this.mapper = mapper;
  }

  public Rilevazione addRilevazione(UUID idPaziente, Double valore) {
    Paziente paziente = pazienti.findById(idPaziente)
        .orElseThrow(() -> new IllegalArgumentException("Paziente non trovato"));

    Rilevazione nrilevazione = Rilevazione.builder()
        .idPaziente(paziente)
        .valore(valore)
        .timestamp(Instant.now())
        .build();

    log.info("Paziente id: " + paziente.getId() + " ha inserito una rilevazione: " + valore);
    return rilevazioni.save(nrilevazione);
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
