package it.univr.glicemiewebapp.service;

import it.univr.glicemiewebapp.entity.Log;

import it.univr.glicemiewebapp.repository.LogRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void info(String message) {
        log.info(message);
        logRepository.save(new Log(null, "INFO", message, Instant.now()));
    }

    public void warn(String message) {
        log.warn(message);
        logRepository.save(new Log(null, "WARN", message, Instant.now()));
    }

    public void error(String message) {
        log.error(message);
        logRepository.save(new Log(null, "ERROR", message, Instant.now()));
    }

}
