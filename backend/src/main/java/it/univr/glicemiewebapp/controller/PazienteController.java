package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.PazienteDTO;
import it.univr.glicemiewebapp.dto.PazienteUtenteDTO;
import it.univr.glicemiewebapp.service.LogService;
import it.univr.glicemiewebapp.service.PazienteService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
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
  private PazienteService pazienteService;
  @Autowired
  private LogService logger;

  @CrossOrigin
  @GetMapping("/all")
  public ResponseEntity<List<PazienteUtenteDTO>> getAll() {

    return ResponseEntity.ok(pazienteService.findAllComplete());

  }

  @PutMapping("/update")
  public ResponseEntity<String> update(@RequestBody PazienteUtenteDTO entity) {

    logger.warn("Attempt to update: " + entity.getId());
    return pazienteService.update(entity);

  }

  @GetMapping("/my/{id}")
  public ResponseEntity<List<PazienteDTO>> getPazientiMedico(@PathVariable("id") UUID idMedico) {
    logger.info("attempt to get patients of " + idMedico);
    return ResponseEntity.ok(pazienteService.findByMedico(idMedico));
  }

}
