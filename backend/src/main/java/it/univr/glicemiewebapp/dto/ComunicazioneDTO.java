package it.univr.glicemiewebapp.dto;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or reading a communication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComunicazioneDTO {

  private UUID id;

  @NotNull(message = "priorità obbligatoria")
  private Integer priorita;

  @NotNull(message = "id paziente obbligatorio")
  private UUID idPaziente;

  @NotNull(message = "descrizione obbligatoria")
  private String descrizione;

  private Instant timestamp;
}
