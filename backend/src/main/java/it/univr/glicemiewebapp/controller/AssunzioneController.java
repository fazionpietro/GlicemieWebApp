package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.repository.AssunzioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assunzioni")
public class AssunzioneController {

    @Autowired
    private AssunzioneRepository assunzioneRepository;
}
