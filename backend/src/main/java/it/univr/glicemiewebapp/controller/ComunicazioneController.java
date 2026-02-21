package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.ComunicazioneDTO;
import it.univr.glicemiewebapp.dto.ComunicazioneMedicoDTO;
import it.univr.glicemiewebapp.entity.Comunicazione;
import it.univr.glicemiewebapp.service.ComunicazioneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

/**
 * Controller for managing patient-doctor communications.
 */
@Slf4j
@RestController
@RequestMapping("/api/comunicazioni")
@RequiredArgsConstructor
public class ComunicazioneController {

  private final ComunicazioneService comunicazioneService;

  @PostMapping
  public ResponseEntity<?> createComunicazione(@RequestBody @Valid ComunicazioneDTO comunicazioneDTO) {
    log.info("Creazione comunicazione per paziente: {}", comunicazioneDTO.getIdPaziente());
    try {
      Comunicazione salvata = comunicazioneService.salvaComunicazione(comunicazioneDTO);
      log.info("Comunicazione salvata con id: {}", salvata.getId());
      return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(salvata));
    } catch (ResponseStatusException e) {
      log.error("Errore durante la creazione della comunicazione: {}", e.getReason());
      return new ResponseEntity<>(e.getReason(), e.getStatusCode());
    } catch (Exception e) {
      log.error("Errore interno durante la creazione della comunicazione", e);
      return new ResponseEntity<>("Errore interno nel server", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/read/{id}")
  public ResponseEntity<String> markAsRead(@PathVariable UUID id) {
    log.info("Lettura comunicazione: {}", id);
  
    return ResponseEntity.ok(comunicazioneService.markAsRead(id));
  }

  private ComunicazioneDTO toDTO(Comunicazione c) {
    return ComunicazioneDTO.builder()
        .id(c.getId())
        .priorita(c.getPriorita())
        .idPaziente(c.getIdPaziente() != null ? c.getIdPaziente().getId() : null)
        .descrizione(c.getDescrizione())
        .timestamp(c.getTimestamp())
        .build();
  }
}
