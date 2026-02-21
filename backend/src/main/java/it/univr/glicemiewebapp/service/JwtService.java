package it.univr.glicemiewebapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import it.univr.glicemiewebapp.entity.BlacklistedToken;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.TokenBlacklistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Service for JWT management: generation, validation, and blacklisting.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

  private final TokenBlacklistRepository tokenBlacklistRepository;
  private final LogService logService;

  @Value("${security.jwt.secret-key}")
  private String secretKey;

  @Value("${security.jwt.expiration-time}")
  private long expirationTimeMs;

  /** Genera un token JWT con subject = email e claim dei ruoli. */
  public String generateToken(Utente utente) {
    return Jwts.builder()
        .setId(UUID.randomUUID().toString())
        .setSubject(utente.getEmail())
        .claim("roles", utente.getAuthorities())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMs))
        .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
        .compact();
  }

  /** Verifica validità del token: firma, scadenza e blacklist. */
  public boolean checkValidity(String token) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(secretKey.getBytes())
          .build()
          .parseClaimsJws(token)
          .getBody();

      return !isTokenBlacklisted(claims.getId());
    } catch (SecurityException e) {
      log.warn("Firma JWT non valida: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.warn("Token JWT malformato: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.warn("Token JWT scaduto: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.warn("Token JWT non supportato: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.warn("Claims JWT vuoti: {}", e.getMessage());
    }
    return false;
  }

  /** Estrae l'email (subject) dal token. */
  public String getMailFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey.getBytes())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  /** Aggiunge il token alla blacklist per impedirne il riutilizzo. */
  public void addToBlacklist(String token) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(secretKey.getBytes())
          .build()
          .parseClaimsJws(token)
          .getBody();

      String jti = claims.getId();
      Instant expiry = claims.getExpiration().toInstant();

      if (jti != null && !tokenBlacklistRepository.existsById(jti)) {
        tokenBlacklistRepository.save(new BlacklistedToken(jti, expiry, Instant.now()));
        logService.info("Token " + jti + " aggiunto alla blacklist");
      }
    } catch (JwtException e) {
      logService.warn("Tentativo di black-listare un token malformato");
    }
  }

  /** Verifica se un token è nella blacklist tramite il suo JTI. */
  public boolean isTokenBlacklisted(String jti) {
    return jti != null && tokenBlacklistRepository.existsById(jti);
  }

  /** Pulizia giornaliera dei token scaduti dalla blacklist (ore 3:00). */
  @Scheduled(cron = "${jobs.blacklist.cleanup.cron:0 0 3 * * *}")
  public void cleanupExpired() {
    tokenBlacklistRepository.deleteAllExpiredSince(Instant.now());
  }
}
