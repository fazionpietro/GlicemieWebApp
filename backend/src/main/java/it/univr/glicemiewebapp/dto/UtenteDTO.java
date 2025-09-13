package it.univr.glicemiewebapp.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
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
    

}
