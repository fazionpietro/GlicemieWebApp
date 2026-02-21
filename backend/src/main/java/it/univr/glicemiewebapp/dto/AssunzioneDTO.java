package it.univr.glicemiewebapp.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for registering a therapy intake.
 */
@Getter
@AllArgsConstructor
public class AssunzioneDTO {

  private final UUID id;
  private final UUID idTerapia;
}
