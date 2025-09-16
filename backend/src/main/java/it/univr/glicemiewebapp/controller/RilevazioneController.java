package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.RilevazioneUtenteDTO;
import it.univr.glicemiewebapp.entity.Rilevazione;
import it.univr.glicemiewebapp.service.RilevazioneService;
import it.univr.glicemiewebapp.dto.RilevazioneUtenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rilevazioni")
public class RilevazioneController {

  private final RilevazioneService rilevazioneService;

  @Autowired
  public RilevazioneController(RilevazioneService rilevazioneService) {
    this.rilevazioneService = rilevazioneService;
  }

  @PostMapping("/{idPaziente}")
  public Rilevazione addRilevazione(
      @PathVariable UUID idPaziente,
      @RequestParam Double valore) {
    return rilevazioneService.addRilevazione(idPaziente, valore);
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
