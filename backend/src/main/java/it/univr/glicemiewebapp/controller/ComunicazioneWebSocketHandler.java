package it.univr.glicemiewebapp.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.univr.glicemiewebapp.dto.ComunicazioneMedicoDTO;
import it.univr.glicemiewebapp.event.NewComunicazioneEvent;
import it.univr.glicemiewebapp.service.ComunicazioneService;
import it.univr.glicemiewebapp.service.LogService;

@Service
public class ComunicazioneWebSocketHandler extends TextWebSocketHandler {

  private static final Map<UUID, WebSocketSession> medicoSessions = new ConcurrentHashMap<>();
  private final ComunicazioneService comunicazioneService;
  private final ObjectMapper objectMapper;
  private final LogService log;

  @Autowired
  public ComunicazioneWebSocketHandler(ComunicazioneService comunicazioneService, ObjectMapper objectMapper,
      LogService log) {
    this.comunicazioneService = comunicazioneService;
    this.objectMapper = objectMapper;
    this.log = log;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    try {

      UUID idMedico = extractIdMedico(session);
      if (idMedico != null) {
        medicoSessions.put(idMedico, session);
        List<ComunicazioneMedicoDTO> comunicazioni = comunicazioneService.getByMedico(idMedico);
        String jsonComunicazioni = objectMapper.writeValueAsString(comunicazioni);
        session.sendMessage(new TextMessage(jsonComunicazioni));
      } else {
        session.sendMessage(new TextMessage("{\"error\": \"ID medico non trovato\"}"));
        session.close();
      }
    } catch (Exception e) {
      session.sendMessage(new TextMessage("{\"error\": \"Errore durante la connessione\"}"));

    }

  }

  @EventListener
  public void onNewComunicazione(NewComunicazioneEvent event) {

    log.info("nuovo alert generato");
    sendToMedico(event.getIdMedico(), event.getComunicazione());
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    UUID idMedico = extractIdMedico(session);
    if (idMedico != null) {
      medicoSessions.remove(idMedico);
    }
  }

  public void sendToMedico(UUID idMedico, ComunicazioneMedicoDTO comunicazione) {

    WebSocketSession session = medicoSessions.get(idMedico);
    if (session != null && session.isOpen()) {
      try {
        String json = objectMapper.writeValueAsString(comunicazione);
        log.info("new message: " + json);
        session.sendMessage(new TextMessage(json));
      } catch (Exception e) {

        log.error("error sendig a comunication to " + idMedico);
      }
    }
  }

  private UUID extractIdMedico(WebSocketSession session) {

    try {
      String uri = session.getUri().toString();
      String[] params = uri.split("\\?");
      if (params.length > 1) {
        String[] queryParams = params[1].split("&");
        for (String param : queryParams) {
          String[] keyValue = param.split("=");
          if (keyValue.length == 2 && "id".equals(keyValue[0])) {
            return UUID.fromString(keyValue[1]);
          }
        }
      }

    } catch (Exception e) {
      System.err.println("Errore nell'estrazione dell'ID medico: " + e.getMessage());
    }
    return null;
  }
}
