package it.univr.glicemiewebapp.forms;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class MedicoAdminForm {

    private String email;
    private String password;
    private String nome;
    private String cognome;

}
