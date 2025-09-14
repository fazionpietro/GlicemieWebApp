package it.univr.glicemiewebapp.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import it.univr.glicemiewebapp.entity.Rilevazione;

public class RilevazioneUtenteDTO {
  private UUID id;
  private double valore;
  private Instant timestamp;
  private String livello;

  public RilevazioneUtenteDTO(Rilevazione r) {
    this.id = r.getId();
    this.valore = r.getValore();
    this.timestamp = r.getTimestamp();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public double getValore() {
    return valore;
  }

  public void setValore(double valore) {
    this.valore = valore;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }

  public String getLivello() {
    return livello;
  }

  public void setLivello(String livello) {
    this.livello = livello;
  }
}
