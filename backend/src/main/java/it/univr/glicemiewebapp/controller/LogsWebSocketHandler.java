package it.univr.glicemiewebapp.controller;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import it.univr.glicemiewebapp.service.LogService;
import it.univr.glicemiewebapp.event.NewLogEvent;

@Service
public class LogsWebSocketHandler extends TextWebSocketHandler {

  private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
  private final LogService logService;

  @Autowired
  public LogsWebSocketHandler(LogService logService) {
    this.logService = logService;
  }

  @EventListener
  public void onNewLog(NewLogEvent evt) {
    broadcast("[" + evt.log() + "]");
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    try {
      sessions.add(session);
      String jsonLogs = logService.allLog(); // ora logService != null
      session.sendMessage(new TextMessage(jsonLogs));
    } catch (Exception e) {
      logService.error("Errore durante l’invio dei log al client: " + e.getMessage());
      session.sendMessage(new TextMessage("ERRORE NELL'INVIO DELLA RISPOSTA"));
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    sessions.remove(session);
  }

  public static void broadcast(String message) {
    sessions.parallelStream()
        .filter(WebSocketSession::isOpen)
        .forEach(s -> {
          try {
            s.sendMessage(new TextMessage(message));
          } catch (Exception ignore) {
          }
        });
  }
}
