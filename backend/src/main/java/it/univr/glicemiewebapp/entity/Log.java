package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Log {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_log", nullable = false)
  private UUID id;

  @NotNull
  @Column(name = "tipo", nullable = false)
  private String tipo;

  @NotNull
  @Column(name = "descrizione", nullable = false, length = Integer.MAX_VALUE)
  private String descrizione;

  @NotNull
  @Column(name = "\"timestamp\"", nullable = false)
  private Instant timestamp;

  public UUID getId() {
    return id;
  }

  private void setId(UUID id) {
    this.id = id;
  }

  public String getDescrizione() {
    return descrizione;
  }

  private void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo() {
    return this.tipo;
  }

  private void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  private void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {

    return "{" +
        "\"id\":\"" + id + "\"," +
        "\"tipo\":\"" + tipo + "\"," +
        "\"descrizione\":\"" + descrizione.replace("\"", "\\\"") + "\"," +
        "\"timestamp\":\"" + timestamp + "\"" +
        "}";
  }

}
