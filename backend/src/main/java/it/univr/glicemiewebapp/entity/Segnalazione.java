package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "segnalazioni")
@NoArgsConstructor
@AllArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@Builder
@ToString

public class Segnalazione {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_segnalazione", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sintomo", nullable = false)
    private Sintomo idSintomo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paziente", nullable = false)
    private Paziente idPaziente;

    @NotNull
    @Column(name = "intensita", nullable = false)
    private Integer intensita;

    @NotNull
    @Column(name = "descrizione", nullable = false, length = Integer.MAX_VALUE)
    private String descrizione;

    @NotNull
    @Column(name = "\"timestamp\"", nullable = false)
    private Instant timestamp;


}