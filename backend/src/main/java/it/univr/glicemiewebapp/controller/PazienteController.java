package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.forms.DeleteRequest;
import it.univr.glicemiewebapp.repository.AssunzioneRepository;
import it.univr.glicemiewebapp.service.PazienteService;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.web.bind.annotation.RequestBody;


@Slf4j

@RestController
@RequestMapping("/api/pazienti")
public class PazienteController {
    @Autowired
    private AssunzioneRepository assunzioneRepository;

    @Autowired
    private PazienteService pazienteService;
    
    @CrossOrigin
    @GetMapping("/all")
    public ResponseEntity<String> getAll() {
        
        try{
            return pazienteService.findAllComplete();
        }catch(ResponseStatusException e){
            log.error(e.getReason(),e.getStatusCode());
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody DeleteRequest req) {
        try {
            return pazienteService.deleteByID(req.getId());

        } catch (ResponseStatusException e) {

            log.error(e.getReason(),e.getStatusCode());
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
            
        }
        
        
    }
    
}
