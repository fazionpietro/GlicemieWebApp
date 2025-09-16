package it.univr.glicemiewebapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstuctor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ComunicazioneDTO{
    private UUID id;

    @NotNull(message = "priorità obbligatoria")
    private Integer priorita;

    @NotNull(message = "id paziente obbligatorio")
    private UUID idpaziente;

    @NotNull(message = "descrizione obbligatoria")
    private string descrizione;

    private instant timestamp;
}