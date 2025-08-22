package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pazienti")
@PrimaryKeyJoinColumn(name = "id_paziente")
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@ToString

public class Paziente extends Utente {

    @NotNull
    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @Column(name = "fattori_rischio", length = Integer.MAX_VALUE)
    private String fattoriRischio;

    @Column(name = "comorbita", length = Integer.MAX_VALUE)
    private String comorbita;

    @Column(name = "patologie_pregresse", length = Integer.MAX_VALUE)
    private String patologiePregresse;


    @OneToMany(mappedBy = "idPaziente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Segnalazione> segnalazioni = new ArrayList<>();

    @OneToMany(mappedBy = "idPaziente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Rilevazione> rilevazioni = new ArrayList<>();

    @OneToMany(mappedBy = "idPaziente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Assunzione> assunzioni = new ArrayList<>();

    @OneToMany(mappedBy = "idPaziente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Terapia> terapie = new ArrayList<>();




    public Paziente(String email, String passwordHash, String nome, String cognome,
                    LocalDate dataNascita) {
        super(null, email, passwordHash, nome, cognome, "ROLE_PAZIENTE");
        this.dataNascita = dataNascita;
    }


    public Paziente(String email, String passwordHash, String nome, String cognome,
                    LocalDate dataNascita, String fattoriRischio, String comorbita,
                    String patologiePregresse) {
        super(null, email, passwordHash, nome, cognome, "ROLE_PAZIENTE");
        this.dataNascita = dataNascita;
        this.fattoriRischio = fattoriRischio;
        this.comorbita = comorbita;
        this.patologiePregresse = patologiePregresse;
    }


}