package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.dto.AssunzioneDTO;
import it.univr.glicemiewebapp.service.AssunzioneService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assunzioni")
public class AssunzioneController {

  @Autowired
  private AssunzioneService assunzioneService;

  @PostMapping("/store")
  public ResponseEntity storeAssumption(@RequestBody List<AssunzioneDTO> request) {

    return ResponseEntity.ok(assunzioneService.create(request));
  }

  @GetMapping("/log/{id}")
  public ResponseEntity getMyAssumptionLog(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(assunzioneService.getAlreadyTaken(id));
  }
}
