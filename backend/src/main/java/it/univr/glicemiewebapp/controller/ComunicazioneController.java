package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.ComunicazioneDTO;
import it.univr.glicemiewebapp.dto.ComunicazioneMedicoDTO;
import it.univr.glicemiewebapp.entity.Comunicazione;
import it.univr.glicemiewebapp.service.ComunicazioneService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comunicazioni")
@RequiredArgsConstructor
public class ComunicazioneController {
  private final ComunicazioneService comunicazioneService;
  private static final Logger logger = LoggerFactory.getLogger(ComunicazioneController.class);

  @PostMapping
  public ResponseEntity<?> createComunicazione(@RequestBody @Valid ComunicazioneDTO comunicazioneDTO) {
    logger.info("creazione comunicazione per paziente: " + comunicazioneDTO.getIdPaziente());

    try {
      Comunicazione comunicazione = Comunicazione.builder().priorita(comunicazioneDTO.getPriorita())
          .descrizione(comunicazioneDTO.getDescrizione()).timestamp(Instant.now()).build();

      Comunicazione comunicazioneSalvata = comunicazioneService.salvaComunicazione(
          comunicazione, comunicazioneDTO.getIdPaziente());

      logger.info("comunicazione salvata con id: {} ", comunicazioneSalvata.getId());
      return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(comunicazioneSalvata));
    } catch (ResponseStatusException e) {
      logger.error("errore durante la creazione della comunicazione: {} ", e.getReason());
      return new ResponseEntity<>(e.getReason(), e.getStatusCode());
    } catch (Exception e) {
      logger.error("Errore durante la creazione della comunicazione: {} ", e);
      return new ResponseEntity<>("Errore interno nel server ", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private ComunicazioneDTO toDTO(Comunicazione comunicazione) {
    return ComunicazioneDTO.builder()
        .id(comunicazione.getId()).priorita(comunicazione.getPriorita())
        .idPaziente(comunicazione.getIdPaziente() != null ? comunicazione.getIdPaziente().getId() : null)
        .descrizione(comunicazione.getDescrizione()).timestamp(comunicazione.getTimestamp()).build();
  }

  @PostMapping("/read/{id}")
  public ResponseEntity<String> markAsRead(@PathVariable UUID id) {
    logger.info("lettura comunicazione: " + id);

    return ResponseEntity.ok(comunicazioneService.markAsRead(id));
  }
}
