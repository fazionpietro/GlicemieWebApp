package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assunzioni")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@ToString
public class Assunzione {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_assunzione", nullable = false)
  private UUID id;

  @NotNull
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_terapia", nullable = false)
  private Terapia idTerapia;

  @NotNull
  @Column(name = "\"timestamp\"", nullable = false)
  private Instant timestamp;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_paziente", nullable = false)
  private Paziente idPaziente;

}
