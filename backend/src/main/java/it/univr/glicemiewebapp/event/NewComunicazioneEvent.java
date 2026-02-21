package it.univr.glicemiewebapp.event;

import java.util.UUID;
import it.univr.glicemiewebapp.dto.ComunicazioneMedicoDTO;

/**
 * Evento pubblicato quando viene creata una nuova comunicazione per un medico.
 */
public record NewComunicazioneEvent(UUID idMedico, ComunicazioneMedicoDTO comunicazione) {
}
