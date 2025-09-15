package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "pazienti")
@PrimaryKeyJoinColumn(name = "id_paziente")
@NoArgsConstructor
@OnDelete(action = OnDeleteAction.CASCADE)
@Getter
@Setter
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

  @JsonIgnore
  @OneToMany
  @JoinColumn(name = "id_paziente")
  private Set<Assunzione> assunzioni = new LinkedHashSet<>();

  @JsonIgnore
  @OneToMany
  @JoinColumn(name = "id_paziente")
  private Set<Rilevazione> rilevazioni = new LinkedHashSet<>();

  @JsonIgnore
  @OneToMany
  @JoinColumn(name = "id_paziente")
  private Set<Segnalazione> segnalazioni = new LinkedHashSet<>();

  @JsonIgnore
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

  public String toString() {

    return super.toString() + " fattoriRischio=" + fattoriRischio + " comorbita=" + comorbita + " patologiePregresse="
        + patologiePregresse;
  }

  @JsonProperty("idMedico")
  public UUID getIdMedico() {
    return idMedico != null ? idMedico.getId() : null;
  }

}
