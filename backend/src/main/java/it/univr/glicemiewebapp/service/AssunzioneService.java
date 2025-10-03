package it.univr.glicemiewebapp.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import it.univr.glicemiewebapp.dto.AssunzioneDTO;
import it.univr.glicemiewebapp.dto.AssunzionePazienteDTO;
import it.univr.glicemiewebapp.entity.Assunzione;
import it.univr.glicemiewebapp.entity.Terapia;
import it.univr.glicemiewebapp.exception.BusinessException;
import it.univr.glicemiewebapp.repository.AssunzioneRepository;
import it.univr.glicemiewebapp.repository.TerapiaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AssunzioneService {
  private final AssunzioneRepository repository;
  private final TerapiaRepository terapiaRepository;

  public String create(List<AssunzioneDTO> request) {

    Instant now = Instant.now();
    Terapia t;
    try {
      for (AssunzioneDTO assunzione : request) {
        t = terapiaRepository.findById(assunzione.getIdTerapia())
            .orElseThrow(() -> new BusinessException("DATA_RETRIEVAL_ERROR", "Failed to find therapy"));
        repository.save(new Assunzione(null, t, now));
      }

      return "Assumption stored correctly";
    } catch (Exception e) {
      throw new BusinessException("CREATION_ERROR", "Failed to store assumption");
    }
  }

  public List<AssunzionePazienteDTO> getAlreadyTaken(UUID id) {

    try {
      ZoneId zone = ZoneId.of("Europe/Rome");
      Instant startOfDay = LocalDate.now(zone).atStartOfDay(zone).toInstant();
      List<AssunzionePazienteDTO> p = repository.findAssunzioniByPazienteId(id, startOfDay);

      return p;
    } catch (Exception e) {
      throw new BusinessException("DATA_RETRIEVAL_ERROR", "Failed to retreive the Assumption Log");
    }

  }
}