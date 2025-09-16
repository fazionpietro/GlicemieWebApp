package it.univr.glicemiewebapp.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.univr.glicemiewebapp.dto.TerapiaDTO;
import it.univr.glicemiewebapp.entity.Terapia;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.exception.BusinessException;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.repository.TerapiaRepository;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class TerapiaService {
  @Autowired
  private final TerapiaRepository terapiaRepository;
  @Autowired
  private final UtenteRepository utenteRepository;
  @Autowired
  private final PazienteRepository pazienteRepository;

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
      throw new BusinessException("DELETION_ERROR", "Failed to delete terapy");
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
      throw new BusinessException("UPDATE_ERROR", "failed to update terapy");
    }
  }

}
