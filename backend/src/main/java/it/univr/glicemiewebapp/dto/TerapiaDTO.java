package it.univr.glicemiewebapp.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for creating or updating a therapy.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TerapiaDTO {

  private UUID id;
  private String farmaco;
  private Integer numAssunzioni;
  private String dosaggio;
  private String indicazioni;
  private UUID idPaziente;
  private UUID idMedico;
}
