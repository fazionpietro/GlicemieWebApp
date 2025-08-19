package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assunzioni")
public class Assunzioni {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_assunzione", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_farmaco", nullable = false)
    private Farmaci idFarmaco;

    @NotNull
    @Column(name = "quantita", nullable = false)
    private Double quantita;

    @NotNull
    @Column(name = "\"timestamp\"", nullable = false)
    private Instant timestamp;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paziente", nullable = false)
    private Pazienti idPaziente;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Farmaci getIdFarmaco() {
        return idFarmaco;
    }

    public void setIdFarmaco(Farmaci idFarmaco) {
        this.idFarmaco = idFarmaco;
    }

    public Double getQuantita() {
        return quantita;
    }

    public void setQuantita(Double quantita) {
        this.quantita = quantita;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Pazienti getIdPaziente() {
        return idPaziente;
    }

    public void setIdPaziente(Pazienti idPaziente) {
        this.idPaziente = idPaziente;
    }

}