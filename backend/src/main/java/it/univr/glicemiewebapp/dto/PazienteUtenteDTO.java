package it.univr.glicemiewebapp.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PazienteUtenteDTO {
  private UUID id;
  private String email;
  private String passwordHash;
  private String nome;
  private String cognome;
  private LocalDate dataNascita;
  private String ruolo;
  private String fattoriRischio;
  private String comorbita;
  private String patologiePregresse;
  private UUID idMedico;
  private String nomeMedico;
  private String cognomeMedico;
  private String emailMedico;

  public PazienteUtenteDTO(UUID id, String email, String nome, String cognome,
      LocalDate dataNascita, String ruolo,
      String fattoriRischio, String comorbita, String patologiePregresse,
      UUID idMedico, String nomeMedico, String cognomeMedico, String emailMedico) {
    this.id = id;
    this.email = email;
    this.passwordHash = null;
    this.nome = nome;
    this.cognome = cognome;
    this.dataNascita = dataNascita;
    this.ruolo = ruolo;
    this.fattoriRischio = fattoriRischio;
    this.comorbita = comorbita;
    this.patologiePregresse = patologiePregresse;
    this.idMedico = idMedico;
    this.nomeMedico = nomeMedico;
    this.cognomeMedico = cognomeMedico;
    this.emailMedico = emailMedico;
  }

}
