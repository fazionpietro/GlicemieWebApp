package it.univr.glicemiewebapp.forms;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@ToString
public class UtenteForm {

    private String email;
    private String password;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;

}
