package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "terapie")
public class Terapie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_terapipa", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_farmaco", nullable = false)
    private Farmaci idFarmaco;

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
    private Pazienti idPaziente;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_curante", nullable = false)
    private Medici medicoCurante;

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

    public Integer getNumAssunzioni() {
        return numAssunzioni;
    }

    public void setNumAssunzioni(Integer numAssunzioni) {
        this.numAssunzioni = numAssunzioni;
    }

    public Double getQuantita() {
        return quantita;
    }

    public void setQuantita(Double quantita) {
        this.quantita = quantita;
    }

    public String getIndicazioni() {
        return indicazioni;
    }

    public void setIndicazioni(String indicazioni) {
        this.indicazioni = indicazioni;
    }

    public Pazienti getIdPaziente() {
        return idPaziente;
    }

    public void setIdPaziente(Pazienti idPaziente) {
        this.idPaziente = idPaziente;
    }

    public Medici getMedicoCurante() {
        return medicoCurante;
    }

    public void setMedicoCurante(Medici medicoCurante) {
        this.medicoCurante = medicoCurante;
    }

}