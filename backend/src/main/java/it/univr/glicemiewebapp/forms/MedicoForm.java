package it.univr.glicemiewebapp.forms;

import lombok.*;

import java.time.LocalDate;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)

public class MedicoForm extends UtenteForm{

    private String ruolo;

    public MedicoForm(String email, String password, String nome, String cognome, LocalDate dataNascita){
        super(email,password,nome,cognome, dataNascita);
        this.ruolo="ROLE_MEDICO";
    }

    @Override
    public String toString() {
        return super.toString()+"ROLE_MEDICO";
    }
}
