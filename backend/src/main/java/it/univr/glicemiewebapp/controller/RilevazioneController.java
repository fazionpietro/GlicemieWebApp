package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.RilevazioneUtenteDTO;
import it.univr.glicemiewebapp.entity.Rilevazione;
import it.univr.glicemiewebapp.service.RilevazioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing glucose level readings.
 * Exposes endpoints to add, retrieve, and delete readings.
 */
@RestController
@RequestMapping("/api/rilevazioni")
@RequiredArgsConstructor
public class RilevazioneController {

  private final RilevazioneService rilevazioneService;

  @PostMapping("/{idPaziente}")
  public Rilevazione addRilevazione(
      @PathVariable UUID idPaziente,
      @RequestParam Double valore,
      @RequestParam Boolean pasto) {
    return rilevazioneService.addRilevazione(idPaziente, valore, pasto);
  }

  @GetMapping("/{idPaziente}")
  public List<Rilevazione> getByPaziente(@PathVariable UUID idPaziente) {
    return rilevazioneService.getByPaziente(idPaziente);
  }

  @DeleteMapping("/{idRilevazione}")
  public void deleteRilevazione(@PathVariable UUID idRilevazione) {
    rilevazioneService.deleteRilevazione(idRilevazione);
  }

  @GetMapping("/dto/{idPaziente}")
  public List<RilevazioneUtenteDTO> getByPazienteDto(@PathVariable UUID idPaziente) {
    return rilevazioneService.getByPazienteDTO(idPaziente);
  }

  @GetMapping("/my/{id}")
  public ResponseEntity<String> getAllByMe(@PathVariable UUID id) {
    try {
      return rilevazioneService.getAllMyRilevazioni(id);
    } catch (ResponseStatusException e) {
      return new ResponseEntity<>(e.getReason(), e.getStatusCode());
    }
  }
}
