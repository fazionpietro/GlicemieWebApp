package it.univr.glicemiewebapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.service.PazienteService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private PazienteService pazienteService;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return "Admin Content.";
    }

    @GetMapping("/medico")
    public String medicoAccess() {
        return "medico Content.";
    }

    
    @GetMapping("/prova")
    public ResponseEntity<String>  prova() {
        
        try{
            return pazienteService.findAllComplete();
        }catch(ResponseStatusException e){
            System.out.println(e);
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }
}