package it.univr.glicemiewebapp.forms;

import lombok.*;

import java.time.LocalDate;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@ToString
public class AdminForm extends UtenteForm{

    private String ruolo;

    public AdminForm(String email, String password, String nome, String cognome, LocalDate dataNascita){
        super(email,password,nome,cognome, dataNascita);
        this.ruolo="ROLE_ADMIN";
    }
}
