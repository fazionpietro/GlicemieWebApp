package it.univr.glicemiewebapp.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PazienteUtenteDTO {
    private UUID id;
    private String email;
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
}