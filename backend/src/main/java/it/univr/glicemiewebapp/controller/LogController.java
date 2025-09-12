package it.univr.glicemiewebapp.controller;

import it.univr.glicemiewebapp.repository.AssunzioneRepository;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {
  @Autowired
  private AssunzioneRepository assunzioneRepository;

}
