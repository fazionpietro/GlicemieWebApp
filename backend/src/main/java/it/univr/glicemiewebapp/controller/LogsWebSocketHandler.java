package it.univr.glicemiewebapp.controller;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import it.univr.glicemiewebapp.service.LogService;
import it.univr.glicemiewebapp.event.NewLogEvent;

/**
 * WebSocket handler for streaming real-time logs to admins.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogsWebSocketHandler extends TextWebSocketHandler {

  private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
  private final LogService logService;

  @EventListener
  public void onNewLog(NewLogEvent evt) {
    broadcast("[" + evt.log() + "]");
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    try {
      // Check if user is authenticated and has ADMIN role
      Object authObj = session.getAttributes().get("authentication");
      if (authObj instanceof org.springframework.security.authentication.UsernamePasswordAuthenticationToken auth) {
        boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));

        if (!isAdmin) {
          log.warn("Unauthorized WebSocket connection attempt to /ws/logs by non-admin user");
          session.sendMessage(
              new TextMessage("{\"error\": \"Accesso negato. Solo gli amministratori possono accedere ai log.\"}"));
          session.close();
          return;
        }
      } else {
        log.warn("WebSocket connection attempt to /ws/logs without authentication");
        session.sendMessage(new TextMessage("{\"error\": \"Autenticazione richiesta\"}"));
        session.close();
        return;
      }

      sessions.add(session);
      String jsonLogs = logService.allLog();
      session.sendMessage(new TextMessage(jsonLogs));
    } catch (Exception e) {
      log.error("Errore invio log al client: {}", e.getMessage());
      session.sendMessage(new TextMessage("ERRORE NELL'INVIO DELLA RISPOSTA"));
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    sessions.remove(session);
  }

  /** Sends a message to all active WebSocket sessions. */
  public static void broadcast(String message) {
    sessions.parallelStream()
        .filter(WebSocketSession::isOpen)
        .forEach(s -> {
          try {
            s.sendMessage(new TextMessage(message));
          } catch (Exception ignored) {
          }
        });
  }
}
