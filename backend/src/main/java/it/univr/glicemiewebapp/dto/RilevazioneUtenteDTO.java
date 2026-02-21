package it.univr.glicemiewebapp.dto;

import it.univr.glicemiewebapp.entity.Rilevazione;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO for displaying a glucose reading to the patient, including the calculated
 * level status.
 */
@Getter
public class RilevazioneUtenteDTO {

  private final UUID id;
  private final double valore;
  private final Boolean dopoPasto;
  private final Instant timestamp;
  private final String livello;

  public RilevazioneUtenteDTO(Rilevazione r) {
    this.id = r.getId();
    this.valore = r.getValore();
    this.timestamp = r.getTimestamp();
    this.dopoPasto = r.getDopoPasto();
    this.livello = calcoloLivello(r.getValore(), this.dopoPasto);
  }

  private String calcoloLivello(double valore, Boolean dopoPasto) {
    if (valore < 80) {
      return "basso";
    } else if (valore > 180 || (valore > 130 && Boolean.FALSE.equals(dopoPasto))) {
      return "alto";
    } else {
      return "normale";
    }
  }
}
