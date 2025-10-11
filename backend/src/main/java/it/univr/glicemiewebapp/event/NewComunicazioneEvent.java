package it.univr.glicemiewebapp.event;

import java.util.UUID;
import it.univr.glicemiewebapp.dto.ComunicazioneMedicoDTO;
import it.univr.glicemiewebapp.entity.Utente;

public record NewComunicazioneEvent(UUID idMedico, ComunicazioneMedicoDTO comunicazione) {

  public UUID getIdMedico() {
    return idMedico;
  }

  public ComunicazioneMedicoDTO getComunicazione() {
    return comunicazione;
  }
}
