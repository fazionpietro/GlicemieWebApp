package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.UtenteDTO;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import it.univr.glicemiewebapp.service.LogService;
import it.univr.glicemiewebapp.service.UtenteService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/utenti")
public class UtenteController {
  @Autowired
  private UtenteRepository repository;

  @Autowired
  private UtenteService utenteService;
  @Autowired
  private LogService logger;

  @GetMapping
  public List<Utente> getAllUtenti() {
    return repository.findAll();
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<Utente> getUtenteById(@PathVariable UUID id) {
    Optional<Utente> result = repository.findById(id);
    return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<Utente> getUtenteByEmail(@PathVariable String email) {
    Optional<Utente> result = repository.findByEmailAddress(email);
    return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/medici/all")
  public ResponseEntity<List<UtenteDTO>> getAll() {

    logger.info("attempt to get all the medics");
    return ResponseEntity.ok(utenteService.getMedici());
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> delete(@PathVariable UUID id) {
    logger.info("attemmpt to delete: " + id);
    return utenteService.deleteByID(id);

  }

  @PutMapping("/update")
  public ResponseEntity<String> updateuser(@RequestBody UtenteDTO entity) {

    return utenteService.update(entity);

  }

}
