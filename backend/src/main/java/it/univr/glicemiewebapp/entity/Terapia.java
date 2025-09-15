package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "terapie")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString

public class Terapia {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_terapia", nullable = false)
  private UUID id;

  @NotNull
  @Column(name = "farmaco", nullable = false)
  private String farmaco;

  @NotNull
  @Column(name = "num_assunzioni", nullable = false)
  private Integer numAssunzioni;

  @NotNull
  @Column(name = "dosaggio", nullable = false)
  private String dosaggio;

  @Column(name = "indicazioni")
  private String indicazioni;

  @NotNull
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_paziente", nullable = false)
  private Paziente idPaziente;

  @NotNull
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "medico_curante", nullable = false)
  private Utente medicoCurante;

  @JsonProperty("idPaziente")
  public UUID getIdPaziente() {
    return idPaziente != null ? idPaziente.getId() : null;
  }

  @JsonProperty("medicoCurante")
  public UUID getMedicoCurante() {
    return medicoCurante != null ? medicoCurante.getId() : null;
  }

}
