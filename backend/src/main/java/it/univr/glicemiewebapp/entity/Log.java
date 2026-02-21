package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity for persistent application logs.
 * Stores log type, description, and timestamp.
 */
@Entity
@Table(name = "logs")
@Getter
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

  /**
   * Costruttore rapido per creare un log con tipo e descrizione (timestamp
   * auto-generato).
   */
  public Log(String tipo, String descrizione) {
    this.tipo = tipo;
    this.descrizione = descrizione;
    this.timestamp = Instant.now();
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
