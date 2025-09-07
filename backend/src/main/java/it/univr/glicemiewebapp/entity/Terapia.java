package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "terapie")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@ToString

public class Terapia {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_terapia", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_farmaco", nullable = false)
    private Farmaco idFarmaco;

    @NotNull
    @Column(name = "num_assunzioni", nullable = false)
    private Integer numAssunzioni;

    @NotNull
    @Column(name = "quantita", nullable = false)
    private Double quantita;

    @Column(name = "indicazioni", length = Integer.MAX_VALUE)
    private String indicazioni;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paziente", nullable = false)
    private Paziente idPaziente;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_curante", nullable = false)
    private Utente medicoCurante;


}