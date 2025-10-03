package it.univr.glicemiewebapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.forms.SignInForm;
import it.univr.glicemiewebapp.repository.UtenteRepository;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @Mock
  private UtenteRepository utenteRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtService jwtService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private LogService logger;

  @InjectMocks
  private AuthenticationService authenticationService;

  private SignInForm signInForm;
  private Utente utente;
  private String email = "test@example.com";
  private String password = "password123";
  private String encodedPassword = "encoded_password";

  @BeforeEach
  void setUp() {
    signInForm = new SignInForm();
    signInForm.setEmail(email);
    signInForm.setPassword(password);

    utente = new Utente();
    utente.setId(UUID.randomUUID());
    utente.setEmail(email);
    utente.setPasswordHash(encodedPassword);
    utente.setRuolo("ROLE_PAZIENTE");
  }

  @Test
  void authentication_Success() {
    // Given
    when(utenteRepository.findByEmailAddress(email)).thenReturn(Optional.of(utente));
    when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
    when(jwtService.generateToken(utente)).thenReturn("jwt_token");

    // When
    ResponseEntity<String> response = authenticationService.authentication(signInForm);

    // Then
    assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    assertTrue(response.getBody().contains("CREDENTIAL VALIDATED"));
    verify(authenticationManager).authenticate(any());
  }

  @Test
  void authentication_UserNotFound() {
    // Given
    when(utenteRepository.findByEmailAddress(email)).thenReturn(Optional.empty());

    // When & Then
    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> authenticationService.authentication(signInForm));
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    assertEquals("INCORRECT EMAIL", exception.getReason());
  }

  @Test
  void authentication_IncorrectPassword() {
    // Given
    when(utenteRepository.findByEmailAddress(email)).thenReturn(Optional.of(utente));
    when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

    // When & Then
    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> authenticationService.authentication(signInForm));
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    assertEquals("INCORRECT PASSWORD", exception.getReason());
  }

  @Test
  void validateToken_Success() {
    // Given
    String token = "valid_token";
    when(jwtService.checkValidity(token)).thenReturn(true);

    // When
    Boolean result = authenticationService.validateToken(token);

    // Then
    assertTrue(result);
    verify(jwtService).checkValidity(token);
  }

  @Test
  void validateToken_Invalid() {
    // Given
    String token = "invalid_token";
    when(jwtService.checkValidity(token)).thenReturn(false);

    // When
    Boolean result = authenticationService.validateToken(token);

    // Then
    assertFalse(result);
  }

  @Test
  void logout_Success() {
    // Given
    String token = "valid_token";
    when(jwtService.checkValidity(token)).thenReturn(true);

    // When
    ResponseEntity<String> response = authenticationService.logout(token);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().contains("LOGOUT SUCCESSFUL"));
    verify(jwtService).addToBlacklist(token);
  }

  @Test
  void logout_InvalidToken() {
    // Given
    String token = "invalid_token";
    when(jwtService.checkValidity(token)).thenReturn(false);

    // When
    ResponseEntity<String> response = authenticationService.logout(token);

    // Then
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("INVALID TOKEN", response.getBody());
  }
}
