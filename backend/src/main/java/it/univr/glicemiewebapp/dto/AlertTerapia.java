package it.univr.glicemiewebapp.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * DTO for therapy adherence alerts.
 * Contains information about patients who missed their therapy.
 */
@Getter
@AllArgsConstructor
@ToString
public class AlertTerapia {

  private final String farmaco;
  private final String nome;
  private final String cognome;
  private final String email;
  private final UUID idTerapia;
  private final UUID idPaziente;
}
