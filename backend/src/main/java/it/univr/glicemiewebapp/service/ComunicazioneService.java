package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.entity.Comunicazione;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.repository.ComunicazioneRepository;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComunicazioneService {
  private final ComunicazioneRepository comunicazioneRepository;
  private final PazienteRepository pazienteRepository;

    @Transactional
    public Comunicazione salvaComunicazione(Comunicazione comunicazione, UUID idPaziente){
        try{
            Paziente paziente =pazienteRepository.findById(idPaziente).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paziente non trovato"));
            comunicazione.setIdPaziente(paziente);
            return comunicazioneRepository.save(comunicazione);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"errore: ");
        }
    }
}
