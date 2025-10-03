package it.univr.glicemiewebapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.univr.glicemiewebapp.dto.PazienteDTO;
import it.univr.glicemiewebapp.dto.PazienteUtenteDTO;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.exception.BusinessException;
import it.univr.glicemiewebapp.exception.ResourceNotFoundException;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.repository.UtenteRepository;

@ExtendWith(MockitoExtension.class)
class PazienteServiceTest {

  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private PazienteRepository pazienteRepository;
  @Mock
  private UtenteRepository utenteRepository;
  @Mock
  private LogService logger;

  @InjectMocks
  private PazienteService pazienteService;

  private final UUID pazienteId = UUID.randomUUID();
  private final UUID medicoId = UUID.randomUUID();

  private PazienteUtenteDTO dto;
  private Paziente paziente;
  private Utente medico;

  @BeforeEach
  void setUp() {
    dto = new PazienteUtenteDTO();
    dto.setId(pazienteId);
    dto.setEmail("test@example.com");
    dto.setIdMedico(medicoId);

    paziente = new Paziente();
    paziente.setId(pazienteId);

    medico = new Utente();
    medico.setId(medicoId);
  }

  /* ==================== happy paths ==================== */

  @Test
  void findAllComplete_Success() {
    List<PazienteUtenteDTO> list = List.of(dto);
    when(pazienteRepository.findAllComplete()).thenReturn(list);

    assertEquals(list, pazienteService.findAllComplete());
  }

  @Test
  void findByMedico_Success() {
    List<PazienteDTO> list = List.of(new PazienteDTO());
    when(utenteRepository.findById(medicoId)).thenReturn(Optional.of(medico));
    when(pazienteRepository.findByIdMedico(medico)).thenReturn(list);

    assertEquals(list, pazienteService.findByMedico(medicoId));
  }

  @Test
  void update_Success() {
    dto.setPasswordHash("secret");
    when(pazienteRepository.findById(pazienteId)).thenReturn(Optional.of(paziente));
    when(utenteRepository.findById(medicoId)).thenReturn(Optional.of(medico));
    when(passwordEncoder.encode("secret")).thenReturn("encoded");

    ResponseEntity<String> resp = pazienteService.update(dto);

    assertEquals("USER UPDATED", resp.getBody());
    verify(pazienteRepository).save(paziente);
  }

  /* ==================== failure paths ==================== */

  @Test
  void findAllComplete_Exception() {
    when(pazienteRepository.findAllComplete()).thenThrow(new RuntimeException("db"));

    BusinessException ex = assertThrows(BusinessException.class,
        () -> pazienteService.findAllComplete());
    assertEquals("DATA_RETRIEVAL_ERROR", ex.getErrorCode());
  }

  @Test
  void findByMedico_Exception() {
    when(utenteRepository.findById(medicoId)).thenThrow(new RuntimeException("db"));

    BusinessException ex = assertThrows(BusinessException.class,
        () -> pazienteService.findByMedico(medicoId));
    assertEquals("DATA_RETRIEVAL_ERROR", ex.getErrorCode());
  }

  @Test
  void update_PazienteNotFound() {
    when(pazienteRepository.findById(pazienteId)).thenReturn(Optional.empty());

    assertThrows(BusinessException.class, () -> pazienteService.update(dto));
  }

  @Test
  void update_MedicoNotFound() {
    when(pazienteRepository.findById(pazienteId)).thenReturn(Optional.of(paziente));
    when(utenteRepository.findById(medicoId)).thenReturn(Optional.empty());

    assertThrows(BusinessException.class, () -> pazienteService.update(dto));
  }

  @Test
  void update_Exception() {
    when(pazienteRepository.findById(pazienteId)).thenThrow(new RuntimeException("Database error"));

    BusinessException exception = assertThrows(BusinessException.class,
        () -> pazienteService.update(dto));
    assertEquals("DATA_RETRIEVAL_ERROR", exception.getErrorCode());
  }
}
