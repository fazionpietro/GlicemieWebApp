package it.univr.glicemiewebapp.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ComunicazioneMedicoDTO {

  private UUID id;
  private Integer priorita;
  private String descrizione;
  private String nome;
  private String cognome;
  private String email;
  private Instant timestamp;

}
