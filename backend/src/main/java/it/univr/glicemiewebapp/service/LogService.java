package it.univr.glicemiewebapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.univr.glicemiewebapp.entity.Log;
import it.univr.glicemiewebapp.event.NewLogEvent;
import it.univr.glicemiewebapp.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Service for application logging: saves logs to DB and publishes them via
 * WebSocket.
 */
@Service
@RequiredArgsConstructor
public class LogService {

  private final LogRepository logRepository;
  private final ObjectMapper objectMapper;
  private final ApplicationEventPublisher eventPublisher;

  public void info(String description) {
    saveLog("INFO", description);
  }

  public void warn(String description) {
    saveLog("WARN", description);
  }

  public void error(String description) {
    saveLog("ERROR", description);
  }

  public String allLog() {
    try {
      List<Log> logs = logRepository.findAll();
      return objectMapper.writeValueAsString(logs);
    } catch (Exception e) {
      return "[]";
    }
  }

  /** Periodic cleanup of logs older than 3 days (every 12 hours). */
  @Scheduled(fixedRate = 43200000)
  public void clearLog() {
    logRepository.deleteAll(
        logRepository.findAll().stream()
            .filter(log -> log.getTimestamp().isBefore(Instant.now().minusSeconds(259200)))
            .toList());
    info("Log periodici eliminati");
  }

  /** Saves log to DB and publishes event for WebSocket notification. */
  private void saveLog(String type, String description) {
    Log log = new Log(type, description);
    logRepository.save(log);
    eventPublisher.publishEvent(new NewLogEvent(log));
  }
}
