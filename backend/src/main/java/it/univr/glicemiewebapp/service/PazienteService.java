package it.univr.glicemiewebapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.univr.glicemiewebapp.dto.*;

import it.univr.glicemiewebapp.repository.PazienteRepository;

@Service
public class PazienteService {

    @Autowired
    private PazienteRepository pazienteRepository;
    

    public List<PazienteUtenteDTO> getAllPazientiCompleto() {
    return pazienteRepository.findAllPazientiCompleto();
}

}
