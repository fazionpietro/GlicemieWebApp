package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.repository.SegnalazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/segnalazioni")
public class SegnalazioneController {

    @Autowired
    private SegnalazioneRepository segnalazioneRepository;

}
