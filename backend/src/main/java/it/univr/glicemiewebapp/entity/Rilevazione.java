package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity representing a glucose level reading (rilevazione).
 * Stores the value, whether it was after a meal, and the timestamp.
 */
@Entity
@Table(name = "rilevazioni")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@ToString
public class Rilevazione {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_rilevazione", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paziente", nullable = false)
    private Paziente idPaziente;

    @NotNull
    @Column(name = "valore", nullable = false)
    private Double valore;

    @NotNull
    @Column(name = "dopo_pasto", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean dopoPasto;

    @NotNull
    @Column(name = "\"timestamp\"", nullable = false)
    private Instant timestamp;

    @JsonProperty("idPaziente")
    public UUID getIdPaziente() {
        return this.idPaziente.getId();
    }

}
