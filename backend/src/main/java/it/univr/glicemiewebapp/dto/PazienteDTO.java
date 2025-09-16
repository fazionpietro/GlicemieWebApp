package it.univr.glicemiewebapp.dto;

import java.time.LocalDate;
import java.util.UUID;

import it.univr.glicemiewebapp.entity.Utente;
import lombok.Builder;
import lombok.Data;

@Data
public class PazienteDTO {
  private UUID id;
  private String email;
  private String nome;
  private String cognome;
  private LocalDate dataNascita;
  private String fattoriRischio;
  private String comorbita;
  private String patologiePregresse;
  private UUID idMedico;

  public PazienteDTO(UUID id, String email, String nome, String cognome, LocalDate dataNascita, String fattoriRischio,
      String comorbita, String patologiePregresse, Utente medico) {
    this.id = id;
    this.email = email;
    this.nome = nome;
    this.cognome = cognome;
    this.dataNascita = dataNascita;
    this.fattoriRischio = fattoriRischio;
    this.comorbita = comorbita;
    this.patologiePregresse = patologiePregresse;
    this.idMedico = medico.getId();
  }

}
