package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.PazienteUtenteDTO;
import it.univr.glicemiewebapp.repository.AssunzioneRepository;
import it.univr.glicemiewebapp.service.LogService;
import it.univr.glicemiewebapp.service.PazienteService;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Slf4j

@RestController
@RequestMapping("/api/pazienti")
public class PazienteController {
  @Autowired
  private AssunzioneRepository assunzioneRepository;

  @Autowired
  private PazienteService pazienteService;
  @Autowired
  private LogService logger;

  @CrossOrigin
  @GetMapping("/all")
  public ResponseEntity<String> getAll() {
    try {
      return pazienteService.findAllComplete();
    } catch (ResponseStatusException e) {
      log.error(e.getReason(), e.getStatusCode());
      return new ResponseEntity<>(e.getReason(), e.getStatusCode());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> putMethodName(@RequestBody PazienteUtenteDTO entity) {

    try {
      logger.warn("Attempt to update: " + entity.getId());
      return pazienteService.update(entity);

    } catch (ResponseStatusException e) {

      log.error(e.getReason(), e.getStatusCode());
      return new ResponseEntity<>(e.getReason(), e.getStatusCode());

    }

  }

  @GetMapping("/my/{id}")
  public ResponseEntity<String> getPazientiMedico(@PathVariable("id") UUID idMedico) {
    try {
      return pazienteService.findByMedico(idMedico);
    } catch (ResponseStatusException e) {
      log.error(e.getReason(), e.getStatusCode());
      return new ResponseEntity<>(e.getReason(), e.getStatusCode());

    }

  }

}
