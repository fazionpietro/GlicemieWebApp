package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "rilevazioni")
public class Rilevazioni {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_segnalazione", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paziente", nullable = false)
    private Pazienti idPaziente;

    @NotNull
    @Column(name = "valore", nullable = false)
    private Double valore;

    @NotNull
    @Column(name = "\"timestamp\"", nullable = false)
    private Instant timestamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Pazienti getIdPaziente() {
        return idPaziente;
    }

    public void setIdPaziente(Pazienti idPaziente) {
        this.idPaziente = idPaziente;
    }

    public Double getValore() {
        return valore;
    }

    public void setValore(Double valore) {
        this.valore = valore;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

}