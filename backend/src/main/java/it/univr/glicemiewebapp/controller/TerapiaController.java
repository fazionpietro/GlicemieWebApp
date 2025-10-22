package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.TerapiaDTO;
import it.univr.glicemiewebapp.service.LogService;
import it.univr.glicemiewebapp.service.TerapiaService;

import java.util.List;
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

@RestController
@RequestMapping("/api/terapie")
public class TerapiaController {

  @Autowired
  private TerapiaService terapiaService;
  @Autowired
  private LogService logger;

  @PutMapping("/new")
  public ResponseEntity<String> create(@RequestBody TerapiaDTO t) {
    logger.info("attempt to create a terapy");
    return terapiaService.create(t);
  }

  @GetMapping("/medico/{id}")
  public ResponseEntity<List<TerapiaDTO>> getByMedico(@PathVariable("id") UUID id) {

    logger.info("attempt to get patients of: " + id);
    return ResponseEntity.ok(terapiaService.getAllByMedico(id));

  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> delete(@PathVariable UUID id) {
    logger.info("attempt to delete terapy: " + id);

    return terapiaService.delete(id);

  }

  @PutMapping("/update/{id}")
  public ResponseEntity<String> update(@PathVariable("id") UUID id, @RequestBody TerapiaDTO t) {
    logger.info("attempt to update terapy: " + id);
    return terapiaService.update(t, id);

  }

  @GetMapping("paziente/{id}")
  public ResponseEntity<List<TerapiaDTO>> getByPaziente(@PathVariable("id") UUID id) {
    logger.info("attempt to get therapies of patient: " + id);
    return ResponseEntity.ok(terapiaService.getAllByPaziente(id));
  }
}
