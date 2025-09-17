package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.ComunicazioneDTO;
import it.univr.glicemiewebapp.entity.Comunicazione;
import it.univr.glicemiewebapp.service.ComunicazioneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/comunicazioni")
@RequiredArgsConstructor
public class ComunicazioneController {

  private final ComunicazioneService comunicazioneService;

  @PostMapping
  public ResponseEntity<ComunicazioneDTO> createComunicazione(@Valid @RequestBody ComunicazioneDTO comunicazioneDTO) {
    Comunicazione comunicazione = Comunicazione.builder().priorita(comunicazioneDTO.getPriorita())
        .descrizione(comunicazioneDTO.getDescrizione())
        .timestamp(Instant.now()).build();

    Comunicazione comunicazioneSalvata = comunicazioneService.salvaComunicazione(comunicazione,
        comunicazioneDTO.getIdPaziente());
    return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(comunicazioneSalvata));
  }

  private ComunicazioneDTO toDTO(Comunicazione comunicazione) {
    return ComunicazioneDTO.builder().id(comunicazione.getId()).priorita(comunicazione.getPriorita())
        .idPaziente(comunicazione.getIdPaziente().getId())
        .descrizione(comunicazione.getDescrizione()).timestamp(comunicazione.getTimestamp()).build();
  }
}
