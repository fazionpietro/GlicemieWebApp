package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.TerapiaDTO;
import it.univr.glicemiewebapp.service.LogService;
import it.univr.glicemiewebapp.service.TerapiaService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing medical therapies.
 * Exposes endpoints to create, update, delete, and retrieve therapies.
 */
@RestController
@RequestMapping("/api/terapie")
@RequiredArgsConstructor
public class TerapiaController {

  private final TerapiaService terapiaService;
  private final LogService logger;

  @PutMapping("/new")
  public ResponseEntity<String> create(@RequestBody TerapiaDTO t) {
    logger.info("Attempt to create a therapy");
    return terapiaService.create(t);
  }

  @GetMapping("/medico/{id}")
  public ResponseEntity<List<TerapiaDTO>> getByMedico(@PathVariable("id") UUID id) {
    logger.info("Attempt to get therapies of medico: " + id);
    return ResponseEntity.ok(terapiaService.getAllByMedico(id));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> delete(@PathVariable UUID id) {
    logger.info("Attempt to delete therapy: " + id);
    return terapiaService.delete(id);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<String> update(@PathVariable("id") UUID id, @RequestBody TerapiaDTO t) {
    logger.info("Attempt to update therapy: " + id);
    return terapiaService.update(t, id);
  }

  @GetMapping("paziente/{id}")
  public ResponseEntity<List<TerapiaDTO>> getByPaziente(@PathVariable("id") UUID id) {
    logger.info("Attempt to get therapies of patient: " + id);
    return ResponseEntity.ok(terapiaService.getAllByPaziente(id));
  }
}
