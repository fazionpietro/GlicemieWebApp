package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "pazienti")
@PrimaryKeyJoinColumn(name = "id_paziente")
@NoArgsConstructor
@OnDelete(action = OnDeleteAction.CASCADE)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@ToString
public class Paziente extends Utente {

    @Column(name = "fattori_rischio", length = Integer.MAX_VALUE)
    private String fattoriRischio;

    @Column(name = "comorbita", length = Integer.MAX_VALUE)
    private String comorbita;

    @Column(name = "patologie_pregresse", length = Integer.MAX_VALUE)
    private String patologiePregresse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico", unique = false, nullable = true)
    private Utente idMedico;

    @OneToMany
    @JoinColumn(name = "id_paziente")
    private Set<Assunzione> assunzioni = new LinkedHashSet<>();

    @OneToMany
    @JoinColumn(name = "id_paziente")
    private Set<Rilevazione> rilevazioni = new LinkedHashSet<>();

    @OneToMany
    @JoinColumn(name = "id_paziente")
    private Set<Segnalazione> segnalazioni = new LinkedHashSet<>();

    @OneToMany
    @JoinColumn(name = "id_paziente")
    private Set<Terapia> terapie = new LinkedHashSet<>();

    public Paziente(String email, String passwordHash, String nome, String cognome,
            LocalDate dataNascita) {
        super(null, email, passwordHash, nome, cognome, dataNascita, "ROLE_PAZIENTE");

    }

    public Paziente(String email, String passwordHash, String nome, String cognome,
            LocalDate dataNascita, String fattoriRischio, String comorbita,
            String patologiePregresse) {
        super(null, email, passwordHash, nome, cognome, dataNascita, "ROLE_PAZIENTE");
        this.fattoriRischio = fattoriRischio;
        this.comorbita = comorbita;
        this.patologiePregresse = patologiePregresse;
    }

}