package it.univr.glicemiewebapp.service;

import io.jsonwebtoken.*;
import it.univr.glicemiewebapp.entity.BlacklistedToken;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.TokenBlacklistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class JwtService {

  @Value("${security.jwt.secret-key}")
  private String secretKey;

  @Value("${security.jwt.expiration-time}")
  private long expirationTimeMs;

  @Autowired
  private TokenBlacklistRepository tokenBlacklistRepository;
  @Autowired
  private LogService logger;

  public String generateToken(Utente utente) {
    String jti = UUID.randomUUID().toString();

    return Jwts.builder()
        .setId(jti)
        .setSubject(utente.getEmail())
        .claim("roles", utente.getAuthorities())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMs))
        .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
        .compact();

  }

  public boolean checkValidity(String token) {

    try {

      Claims claims = Jwts.parserBuilder()
          .setSigningKey(secretKey.getBytes())
          .build()
          .parseClaimsJws(token)
          .getBody();

      String jti = claims.getId();

      if (isTokenBlacklisted(jti)) {
        return false;
      }

      Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
      return true;

    } catch (SecurityException e) {

      System.out.println("Invalid JWT signature: " + e.getMessage());
    } catch (MalformedJwtException e) {
      System.out.println("Invalid JWT token: " + e.getMessage());

    } catch (ExpiredJwtException e) {
      System.out.println("JWT token is expired: " + e.getMessage());

    } catch (UnsupportedJwtException e) {
      System.out.println("JWT token is unsupported: " + e.getMessage());

    } catch (IllegalArgumentException e) {
      System.out.println("JWT claims string is empty: " + e.getMessage());
    }

    return false;

  }

  public String getMailFromToken(String token) {

    return Jwts.parserBuilder()
        .setSigningKey(secretKey.getBytes())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();

  }

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
        logger.info("Token " + jti + " aggiunto alla blacklist");

      }

    } catch (JwtException e) {
      logger.warn("Tentativo di black-listare un token malformato");

    }

  }

  public boolean isTokenBlacklisted(String jti) {
    return jti != null && tokenBlacklistRepository.existsById(jti);

  }

  @Scheduled(cron = "${jobs.blacklist.cleanup.cron:0 0 3 * * *}")
  public void cleanupExpired() {

    tokenBlacklistRepository.deleteAllExpiredSince(Instant.now());

  }

}
