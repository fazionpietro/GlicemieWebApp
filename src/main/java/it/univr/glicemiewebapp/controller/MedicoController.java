package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medici")
public class MedicoController {
    @Autowired
    private MedicoRepository medicoRepository;
}
