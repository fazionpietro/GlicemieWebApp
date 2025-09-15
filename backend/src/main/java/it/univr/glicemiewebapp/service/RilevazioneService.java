package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.entity.Rilevazione;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.repository.RilevazioneRepository;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.dto.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import it.univr.glicemiewebapp.dto.RilevazioneUtenteDTO;

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

  public RilevazioneService(RilevazioneRepository rilevazioni, PazienteRepository pazienti) {
    this.rilevazioni = rilevazioni;
    this.pazienti = pazienti;
  }

  public Rilevazione addRilevazione(UUID idPaziente, Double valore) {
    Paziente paziente = pazienti.findById(idPaziente)
        .orElseThrow(() -> new IllegalArgumentException("Paziente non trovato"));

    Rilevazione nrilevazione = Rilevazione.builder()
        .idPaziente(paziente)
        .valore(valore)
        .timestamp(Instant.now())
        .build();

    log.info("Paziente id: " + paziente.getId()+"ha inserito una rilevazione" + valore);
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

}
