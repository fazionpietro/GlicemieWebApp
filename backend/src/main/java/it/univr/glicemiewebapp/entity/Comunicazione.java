package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing a communication/message between a patient and the
 * system/doctor.
 * Contains priority, description, read status, and a timestamp.
 */
@Entity
@Table(name = "comunicazioni")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Comunicazione {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_comunicazione", nullable = false)
  private UUID id;

  @NotNull
  @Column(name = "priorita", nullable = false)
  private Integer priorita;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_paziente", nullable = false)
  private Paziente idPaziente;

  @NotNull
  @Column(name = "descrizione", nullable = false, length = Integer.MAX_VALUE)
  private String descrizione;

  @Column(name = "letto", nullable = false, columnDefinition = "boolean default false")
  private boolean letto;

  @NotNull
  @Column(name = "\"timestamp\"", nullable = false)
  private Instant timestamp;

}
