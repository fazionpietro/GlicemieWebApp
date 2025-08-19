package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pazienti")
public class Pazienti {
    @Id
    @Column(name = "id_paziente", nullable = false)
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_paziente", nullable = false)
    private Utenti utenti;

    @NotNull
    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @Column(name = "fattori_rischio", length = Integer.MAX_VALUE)
    private String fattoriRischio;

    @Column(name = "comorbita", length = Integer.MAX_VALUE)
    private String comorbita;

    @Column(name = "patologie_presse", length = Integer.MAX_VALUE)
    private String patologiePresse;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Utenti getUtenti() {
        return utenti;
    }

    public void setUtenti(Utenti utenti) {
        this.utenti = utenti;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getFattoriRischio() {
        return fattoriRischio;
    }

    public void setFattoriRischio(String fattoriRischio) {
        this.fattoriRischio = fattoriRischio;
    }

    public String getComorbita() {
        return comorbita;
    }

    public void setComorbita(String comorbita) {
        this.comorbita = comorbita;
    }

    public String getPatologiePresse() {
        return patologiePresse;
    }

    public void setPatologiePresse(String patologiePresse) {
        this.patologiePresse = patologiePresse;
    }

}