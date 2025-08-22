package it.univr.glicemiewebapp.forms;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class PazienteForm extends MedicoAdminForm {
    private LocalDate dataNascita;
    private String fattoriRischio;
    private String comorbita;
    private String patologiePregresse;


    public PazienteForm(String email, String passwordHash, String nome, String cognome,
                        LocalDate dataNascita, String fattoriRischio, String comorbita,
                        String patologiePregresse) {
        super(email, passwordHash, nome, cognome);
        this.dataNascita = dataNascita;
        this.fattoriRischio = fattoriRischio;
        this.comorbita = comorbita;
        this.patologiePregresse = patologiePregresse;

    }
}
