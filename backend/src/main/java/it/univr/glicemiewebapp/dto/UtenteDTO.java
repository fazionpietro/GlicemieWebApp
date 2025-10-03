package it.univr.glicemiewebapp.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UtenteDTO {

  private UUID id;
  private String email;
  private String passwordHash;
  private String nome;
  private String cognome;
  private LocalDate dataNascita;

  public UtenteDTO(UUID id, String email, String nome, String cognome, LocalDate dataNascita) {

    this.id = id;
    this.email = email;
    this.passwordHash = null;
    this.nome = nome;
    this.cognome = cognome;
    this.dataNascita = dataNascita;
  }

}
