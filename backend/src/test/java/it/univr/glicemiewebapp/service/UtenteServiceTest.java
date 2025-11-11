package it.univr.glicemiewebapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.univr.glicemiewebapp.dto.UtenteDTO;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.exception.BusinessException;
import it.univr.glicemiewebapp.exception.ValidationException;
import it.univr.glicemiewebapp.repository.UtenteRepository;

@ExtendWith(MockitoExtension.class)
public class UtenteServiceTest {

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UtenteRepository utenteRepository;
  @Mock
  private LogService logger;

  @InjectMocks
  private UtenteService utenteService;

  private Utente utente;
  private UtenteDTO utenteDTO;
  private UUID utenteId;
  private String email;

  @BeforeEach
  void setUp() {
    utenteId = UUID.randomUUID();
    email = "test@example.com";

    utente = Utente.builder()
        .id(utenteId)
        .email(email)
        .nome("Test")
        .cognome("User")
        .passwordHash("hashedpassword")
        .dataNascita(LocalDate.of(1990, 1, 1))
        .ruolo("ROLE_PAZIENTE")
        .build();

    utenteDTO = new UtenteDTO();
    utenteDTO.setId(utenteId);
    utenteDTO.setEmail("new@example.com");
    utenteDTO.setNome("NewName");
    utenteDTO.setCognome("NewSurname");
    utenteDTO.setPasswordHash("newPassword123");
    utenteDTO.setDataNascita(LocalDate.of(1995, 5, 5));
  }

  @Test
  void getAll_Success() {
    when(utenteRepository.findAll()).thenReturn(Arrays.asList(utente));
    List<Utente> result = utenteService.getAll();
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(utenteRepository).findAll();
  }

  @Test
  void getById_Success() {
    when(utenteRepository.findById(utenteId)).thenReturn(Optional.of(utente));
    Optional<Utente> result = utenteService.getById(utenteId);
    assertTrue(result.isPresent());
    assertEquals(utente, result.get());
    verify(utenteRepository).findById(utenteId);
  }

  @Test
  void getById_NotFound() {
    when(utenteRepository.findById(utenteId)).thenReturn(Optional.empty());
    Optional<Utente> result = utenteService.getById(utenteId);
    assertFalse(result.isPresent());
    verify(utenteRepository).findById(utenteId);
  }

  @Test
  void getByEmail_Success() {
    when(utenteRepository.findByEmailAddress(email)).thenReturn(Optional.of(utente));
    Optional<Utente> result = utenteService.getByEmail(email);
    assertTrue(result.isPresent());
    assertEquals(email, result.get().getEmail());
    verify(utenteRepository).findByEmailAddress(email);
  }

  @Test
  void getMedici_Success() {
    List<UtenteDTO> medici = Arrays.asList(new UtenteDTO());
    when(utenteRepository.findByRole("ROLE_MEDICO")).thenReturn(medici);
    List<UtenteDTO> result = utenteService.getMedici();
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(utenteRepository).findByRole("ROLE_MEDICO");
  }

  @Test
  void getMedici_Failure() {
    when(utenteRepository.findByRole("ROLE_MEDICO")).thenThrow(new RuntimeException("DB Error"));
    BusinessException exception = assertThrows(BusinessException.class,
        () -> utenteService.getMedici());
    assertEquals("DATA_RETRIEVAL_ERROR", exception.getErrorCode());
    verify(utenteRepository).findByRole("ROLE_MEDICO");
  }

  @Test
  void deleteByID_Success() {
    doNothing().when(utenteRepository).deleteById(utenteId);
    ResponseEntity<String> response = utenteService.deleteByID(utenteId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{message: \"UTENTE ELIMINATO\" }", response.getBody());
    verify(utenteRepository).deleteById(utenteId);
  }

  @Test
  void deleteByID_Failure() {
    doThrow(new RuntimeException("DB Error")).when(utenteRepository).deleteById(utenteId);
    BusinessException exception = assertThrows(BusinessException.class,
        () -> utenteService.deleteByID(utenteId));
    assertEquals("DELETION_ERROR", exception.getErrorCode());
    verify(utenteRepository).deleteById(utenteId);
  }

  @Test
  void updateUtenteDTO_Success() {
    when(utenteRepository.findById(utenteId)).thenReturn(Optional.of(utente));
    when(utenteRepository.existsByEmail(utenteDTO.getEmail())).thenReturn(false);
    when(passwordEncoder.encode(utenteDTO.getPasswordHash())).thenReturn("encodedNewPassword");

    ResponseEntity<String> response = utenteService.update(utenteDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("USER UPDATED", response.getBody());
    verify(utenteRepository).findById(utenteId);
    verify(utenteRepository).existsByEmail(utenteDTO.getEmail());
    verify(passwordEncoder).encode(utenteDTO.getPasswordHash());

    assertEquals(utenteDTO.getEmail(), utente.getEmail());
    assertEquals(utenteDTO.getNome(), utente.getNome());
    assertEquals(utenteDTO.getCognome(), utente.getCognome());
    assertEquals(utenteDTO.getDataNascita(), utente.getDataNascita());
    assertEquals("encodedNewPassword", utente.getPasswordHash());
  }

  @Test
  void updateUtenteDTO_UserNotFound() {
    when(utenteRepository.findById(utenteId)).thenReturn(Optional.empty());

    BusinessException exception = assertThrows(BusinessException.class,
        () -> utenteService.update(utenteDTO));

    assertEquals("DATA_RETRIEVAL_ERROR", exception.getErrorCode());
    verify(utenteRepository).findById(utenteId);
  }

  @Test
  void updateUtenteDTO_EmailExists() {
    when(utenteRepository.findById(utenteId)).thenReturn(Optional.of(utente));
    when(utenteRepository.existsByEmail(utenteDTO.getEmail())).thenReturn(true);

    ValidationException exception = assertThrows(ValidationException.class,
        () -> utenteService.update(utenteDTO));

    assertEquals("Email already exists", exception.getMessage());

    verify(utenteRepository).existsByEmail(utenteDTO.getEmail());
    verify(passwordEncoder, never()).encode(anyString());
  }

  @Test
  void updateUtenteDTO_PartialUpdate_NoPassword() {
    utenteDTO.setPasswordHash(null);
    utenteDTO.setEmail(utente.getEmail());

    when(utenteRepository.findById(utenteId)).thenReturn(Optional.of(utente));

    ResponseEntity<String> response = utenteService.update(utenteDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(utenteRepository, never()).existsByEmail(anyString());
    verify(passwordEncoder, never()).encode(anyString());
    assertEquals(utenteDTO.getNome(), utente.getNome());
    assertEquals("hashedpassword", utente.getPasswordHash());
  }
}
