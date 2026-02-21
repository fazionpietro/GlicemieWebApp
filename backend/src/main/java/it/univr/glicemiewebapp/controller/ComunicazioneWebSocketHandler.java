package it.univr.glicemiewebapp.controller;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.List;

/**
 * WebSocket handler for real-time notifications to doctors regarding
 * communications.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ComunicazioneWebSocketHandler extends TextWebSocketHandler {

  private static final Map<UUID, WebSocketSession> medicoSessions = new ConcurrentHashMap<>();
  private final ComunicazioneService comunicazioneService;
  private final ObjectMapper objectMapper;
  private final LogService logService;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    try {
      // Check if user is authenticated and has MEDICO role
      Object authObj = session.getAttributes().get("authentication");
      if (authObj instanceof org.springframework.security.authentication.UsernamePasswordAuthenticationToken auth) {
        boolean isMedico = auth.getAuthorities().stream()
            .anyMatch(grantedAuthority -> "ROLE_MEDICO".equals(grantedAuthority.getAuthority()));

        if (!isMedico) {
          log.warn("Unauthorized WebSocket connection attempt to /ws/comunicazioni by non-medico user");
          session.sendMessage(
              new TextMessage("{\"error\": \"Accesso negato. Solo i medici possono accedere alle comunicazioni.\"}"));
          session.close();
          return;
        }
      } else {
        log.warn("WebSocket connection attempt to /ws/comunicazioni without authentication");
        session.sendMessage(new TextMessage("{\"error\": \"Autenticazione richiesta\"}"));
        session.close();
        return;
      }

      UUID idMedico = extractIdMedico(session);
      if (idMedico != null) {
        medicoSessions.put(idMedico, session);
        List<ComunicazioneMedicoDTO> comunicazioni = comunicazioneService.getByMedico(idMedico);
        String json = objectMapper.writeValueAsString(comunicazioni);
        session.sendMessage(new TextMessage(json));
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
    logService.info("Nuovo alert generato");
    sendToMedico(event.idMedico(), event.comunicazione());
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    UUID idMedico = extractIdMedico(session);
    if (idMedico != null) {
      medicoSessions.remove(idMedico);
    }
  }

  /** Sends a communication via WebSocket to the specified doctor. */
  public void sendToMedico(UUID idMedico, ComunicazioneMedicoDTO comunicazione) {
    WebSocketSession session = medicoSessions.get(idMedico);
    if (session != null && session.isOpen()) {
      try {
        String json = objectMapper.writeValueAsString(comunicazione);
        session.sendMessage(new TextMessage(json));
      } catch (Exception e) {
        log.error("Errore invio comunicazione al medico {}", idMedico, e);
      }
    }
  }

  /**
   * Extracts the doctor's ID from the "id" query parameter of the WebSocket URI.
   */
  private UUID extractIdMedico(WebSocketSession session) {
    try {
      String uri = session.getUri().toString();
      String[] params = uri.split("\\?");
      if (params.length > 1) {
        for (String param : params[1].split("&")) {
          String[] keyValue = param.split("=");
          if (keyValue.length == 2 && "id".equals(keyValue[0])) {
            return UUID.fromString(keyValue[1]);
          }
        }
      }
    } catch (Exception e) {
      log.error("Errore estrazione ID medico: {}", e.getMessage());
    }
    return null;
  }
}
