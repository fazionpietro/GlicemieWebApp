package it.univr.glicemiewebapp.forms;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@ToString
public class PazienteForm extends UtenteForm {

  private String fattoriRischio;
  private String comorbita;
  private String patologiePregresse;
  private String ruolo;
  private UUID idMedico;

  public PazienteForm(String email, String passwordHash, String nome, String cognome,
      String fattoriRischio, String comorbita,
      String patologiePregresse, LocalDate dataNascita, UUID idMEdico) {
    super(email, passwordHash, nome, cognome, dataNascita);

    this.fattoriRischio = fattoriRischio;
    this.comorbita = comorbita;
    this.patologiePregresse = patologiePregresse;
    this.ruolo = "ROLE_PAZIENTE";
    this.idMedico = idMedico;

  }

}
