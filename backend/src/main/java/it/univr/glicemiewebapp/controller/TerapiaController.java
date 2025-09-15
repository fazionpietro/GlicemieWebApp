package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.TerapiaDTO;
import it.univr.glicemiewebapp.repository.AssunzioneRepository;
import it.univr.glicemiewebapp.service.LogService;
import it.univr.glicemiewebapp.service.TerapiaService;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/terapie")
public class TerapiaController {

  @Autowired
  private TerapiaService terapiaService;
  @Autowired
  private LogService logger;

  @PostMapping("/new")
  public ResponseEntity<String> create(@RequestBody TerapiaDTO t) {
    try {
      return terapiaService.create(t);
    } catch (ResponseStatusException e) {
      logger.error(e.getReason() + " " + e.getStatusCode());
      return new ResponseEntity<>(e.getReason(), e.getStatusCode());
    }
  }

  @GetMapping("/medico/{id}")
  public ResponseEntity<String> getByMedico(@PathVariable("id") UUID id) {
    try {
      return terapiaService.getAllByMedico(id);
    } catch (ResponseStatusException e) {
      logger.error(e.getReason() + " " + e.getStatusCode());
      return new ResponseEntity<>(e.getReason(), e.getStatusCode());
    }

  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> delete(@PathVariable UUID id) {
    try {
      return terapiaService.delete(id);
    } catch (ResponseStatusException e) {
      logger.error(e.getReason() + " " + e.getStatusCode());
      return new ResponseEntity<>(e.getReason(), e.getStatusCode());
    }

  }

  @PutMapping("/update/{id}")
  public ResponseEntity<String> update(@PathVariable("id") UUID id, @RequestBody TerapiaDTO t) {
    try {
      return terapiaService.update(t, id);
    } catch (ResponseStatusException e) {
      logger.error(e.getReason() + " " + e.getStatusCode());
      return new ResponseEntity<>(e.getReason(), e.getStatusCode());
    }

  }
}
