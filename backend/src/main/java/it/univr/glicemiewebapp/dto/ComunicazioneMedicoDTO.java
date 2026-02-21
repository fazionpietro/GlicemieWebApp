package it.univr.glicemiewebapp.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for communications viewed by a doctor (includes patient details).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComunicazioneMedicoDTO {

  private UUID id;
  private Integer priorita;
  private String descrizione;
  private String nome;
  private String cognome;
  private String email;
  private Instant timestamp;
}
