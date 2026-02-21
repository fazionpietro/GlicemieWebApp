package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.UtenteDTO;
import it.univr.glicemiewebapp.service.LogService;
import it.univr.glicemiewebapp.service.UtenteService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing users (Doctors, Admins).
 * Exposes endpoints to retrieve list of doctors, update user details, and
 * delete users.
 */
@RestController
@RequestMapping("/api/utenti")
@RequiredArgsConstructor
public class UtenteController {

  private final UtenteService utenteService;
  private final LogService logger;

  @GetMapping("/medici/all")
  public ResponseEntity<List<UtenteDTO>> getAll() {
    logger.info("Attempt to get all medics");
    return ResponseEntity.ok(utenteService.getMedici());
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> delete(@PathVariable UUID id) {
    logger.info("Attempt to delete: " + id);
    return utenteService.deleteByID(id);
  }

  @PutMapping("/update")
  public ResponseEntity<String> update(@RequestBody UtenteDTO entity) {
    return utenteService.update(entity);
  }
}
