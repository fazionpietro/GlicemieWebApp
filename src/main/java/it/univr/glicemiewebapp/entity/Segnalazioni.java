package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "segnalazioni")
public class Segnalazioni {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_segnalazione", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sintomo", nullable = false)
    private Sintomi idSintomo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paziente", nullable = false)
    private Pazienti idPaziente;

    @NotNull
    @Column(name = "intensita", nullable = false)
    private Integer intensita;

    @NotNull
    @Column(name = "descrizione", nullable = false, length = Integer.MAX_VALUE)
    private String descrizione;

    @NotNull
    @Column(name = "\"timestamp\"", nullable = false)
    private Instant timestamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Sintomi getIdSintomo() {
        return idSintomo;
    }

    public void setIdSintomo(Sintomi idSintomo) {
        this.idSintomo = idSintomo;
    }

    public Pazienti getIdPaziente() {
        return idPaziente;
    }

    public void setIdPaziente(Pazienti idPaziente) {
        this.idPaziente = idPaziente;
    }

    public Integer getIntensita() {
        return intensita;
    }

    public void setIntensita(Integer intensita) {
        this.intensita = intensita;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

}