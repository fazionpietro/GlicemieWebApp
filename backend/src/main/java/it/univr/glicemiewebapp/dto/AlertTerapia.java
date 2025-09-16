package it.univr.glicemiewebapp.dto;

import java.time.Instant;
import java.util.UUID;

public class AlertTerapia {
  private final String farmaco;
  private final String nome;
  private final String cognome;
  private final String email;
  private final UUID idTerapia;
  private final UUID idPaziente;

  public AlertTerapia(String farmaco, String nome, String cognome, String email, UUID idTerapia, UUID idPaziente) {
    this.farmaco = farmaco;
    this.nome = nome;
    this.cognome = cognome;
    this.email = email;
    this.idTerapia = idTerapia;
    this.idPaziente = idPaziente;
  }

  // Getters
  public String getFarmaco() {
    return farmaco;
  }

  public String getNome() {
    return nome;
  }

  public String getCognome() {
    return cognome;
  }

  public String getEmail() {
    return email;
  }

  public UUID getIdTerapia() {
    return idTerapia;
  }

  public UUID getIdPaziente() {
    return idPaziente;
  }
}
