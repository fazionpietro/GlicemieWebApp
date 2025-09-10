package it.univr.glicemiewebapp.service;



import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;


import it.univr.glicemiewebapp.repository.PazienteRepository;

@Service
public class PazienteService {

    @Autowired
    private PazienteRepository pazienteRepository;

    @Autowired
    private ObjectMapper mapper; 

    public  ResponseEntity<String> findAllComplete() {
        try {
            return new ResponseEntity<>(mapper.writeValueAsString(pazienteRepository.findAllComplete()), HttpStatus.OK);
            
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED TO RETRIVE DATA");
        }
    }

}
