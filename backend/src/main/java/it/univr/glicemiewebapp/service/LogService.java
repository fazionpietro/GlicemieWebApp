package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.entity.Log;

import it.univr.glicemiewebapp.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogService {

  private final LogRepository logRepository;
  private final ObjectMapper mapper;
  private final ApplicationEventPublisher publisher; // Spring lo injetta

  public void info(String message) {
    saveAndPublish("INFO", message);
  }

  public void warn(String message) {
    saveAndPublish("WARN", message);
  }

  public void error(String message) {
    saveAndPublish("ERROR", message);
  }

  private void saveAndPublish(String level, String message) {
    Log logEntity = new Log(null, level, message, Instant.now());
    logRepository.save(logEntity);
    publisher.publishEvent(new NewLogEvent(logEntity));
  }

  public String allLog() throws JsonProcessingException {
    return mapper.writeValueAsString(logRepository.findAll());
  }

  @Scheduled(cron = "0 */10 * * * *", zone = "Europe/Berlin")
  public void clearLog() {

    log.info("cock doppio");
    logRepository.deleteAll();
  }
}
