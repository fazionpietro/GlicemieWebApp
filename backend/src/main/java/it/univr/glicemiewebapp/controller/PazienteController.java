package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.PazienteDTO;
import it.univr.glicemiewebapp.dto.PazienteUtenteDTO;
import it.univr.glicemiewebapp.service.LogService;
import it.univr.glicemiewebapp.service.PazienteService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing patients.
 * Exposes endpoints to retrieve patient lists, update details, and get patients
 * by doctor.
 */
@RestController
@RequestMapping("/api/pazienti")
@RequiredArgsConstructor
public class PazienteController {

  private final PazienteService pazienteService;
  private final LogService logger;

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
    logger.info("Attempt to get patients of " + idMedico);
    return ResponseEntity.ok(pazienteService.findByMedico(idMedico));
  }
}
