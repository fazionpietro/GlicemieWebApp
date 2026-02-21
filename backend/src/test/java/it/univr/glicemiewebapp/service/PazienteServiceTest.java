package it.univr.glicemiewebapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import it.univr.glicemiewebapp.entity.Utente;

import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.repository.UtenteRepository;
import it.univr.glicemiewebapp.dto.PazienteDTO;
import it.univr.glicemiewebapp.dto.PazienteUtenteDTO;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class PazienteServiceTest {

  @Mock
  PasswordEncoder passwordEncoder;
  @Mock
  PazienteRepository pazienteRepository;
  @Mock
  UtenteRepository utenteRepository;
  @Mock
  LogService logger;
  @InjectMocks
  PazienteService service;

  @BeforeEach
  void setUp() {
    Mockito.reset(passwordEncoder, passwordEncoder, utenteRepository, logger);
  }

  @Test
  void findAllComplete_success() {
    List<PazienteUtenteDTO> expected = List.of(mock(PazienteUtenteDTO.class));
    when(pazienteRepository.findAllComplete()).thenReturn(expected);

    List<PazienteUtenteDTO> result = pazienteRepository.findAllComplete();
    assertSame(expected, result);
    verify(pazienteRepository).findAllComplete();
    verifyNoMoreInteractions(pazienteRepository);
  }

  @Test
  void findAllComplete_exception() {

    when(pazienteRepository.findAllComplete()).thenThrow(new RuntimeException());

    BusinessException ex = assertThrows(BusinessException.class, () -> service.findAllComplete());
    assertTrue(ex.getMessage().contains("Failed to retrieve patients data"));

  }

  @Test
  void findByMedico_success() {
    UUID medicoId = UUID.randomUUID();
    Utente medico = mock(Utente.class);
    when(utenteRepository.findById(medicoId)).thenReturn(Optional.of(medico));

    List<PazienteDTO> expected = List.of(mock(PazienteDTO.class));
    when(pazienteRepository.findByIdMedico(medico)).thenReturn(expected);

    List<PazienteDTO> actual = service.findByMedico(medicoId);
    assertSame(expected, actual);
    verify(utenteRepository).findById(medicoId);
    verify(pazienteRepository).findByIdMedico(medico);
    verifyNoMoreInteractions(pazienteRepository);

  }

  @Test
  void findByMedico_exception() {
    UUID medicoId = UUID.randomUUID();
    when(utenteRepository.findById(medicoId)).thenReturn(Optional.empty());

    BusinessException ex = assertThrows(BusinessException.class, () -> service.findByMedico(medicoId));
    assertTrue(ex.getMessage().contains("Failed to retrieve patients for medico"));

  }

  @Test
  void update_ok_updatesFields_medico_and_password() {
    UUID pazId = UUID.randomUUID();
    UUID medicoId = UUID.randomUUID();

    PazienteUtenteDTO request = mock(PazienteUtenteDTO.class);
    when(request.getId()).thenReturn(pazId);
    when(request.getEmail()).thenReturn("new@example.com");
    when(request.getNome()).thenReturn("Mario");
    when(request.getCognome()).thenReturn("Rossi");
    when(request.getDataNascita()).thenReturn(LocalDate.of(2004, 6, 24));
    when(request.getFattoriRischio()).thenReturn("Fumo");
    when(request.getComorbita()).thenReturn("Ipertensione");
    when(request.getPatologiePregresse()).thenReturn("Asma");
    when(request.getIdMedico()).thenReturn(medicoId);
    when(request.getPasswordHash()).thenReturn("plaintext");

    Paziente paziente = mock(Paziente.class);
    when(pazienteRepository.findById(pazId)).thenReturn(Optional.of(paziente));

    Utente medico = mock(Utente.class);
    when(utenteRepository.findById(medicoId)).thenReturn(Optional.of(medico));
    when(passwordEncoder.encode("plaintext")).thenReturn("ENC");

    ResponseEntity<String> resp = service.update(request);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
    assertEquals("USER UPDATED", resp.getBody());

    // verify field updates
    verify(logger).warn(startsWith("Attempt to modify user: "));
    verify(paziente).setEmail("new@example.com");
    verify(paziente).setNome("Mario");
    verify(paziente).setCognome("Rossi");
    verify(paziente).setDataNascita(any(LocalDate.class));
    verify(paziente).setFattoriRischio("Fumo");
    verify(paziente).setComorbita("Ipertensione");
    verify(paziente).setPatologiePregresse("Asma");
    verify(paziente).setIdMedico(medico);
    verify(paziente).setPasswordHash("ENC");
    verify(pazienteRepository).save(paziente);
  }

  @Test
  void update_notFound_patient_throwsResponseStatusException404() {
    UUID id = UUID.randomUUID();
    PazienteUtenteDTO request = mock(PazienteUtenteDTO.class);
    when(request.getId()).thenReturn(id);
    when(pazienteRepository.findById(id)).thenReturn(Optional.empty());

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.update(request));
    assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
  }

  @Test
  void update_idMedico_notFound_wrappedIntoBusinessException() {
    UUID pazId = UUID.randomUUID();
    UUID medicoId = UUID.randomUUID();

    PazienteUtenteDTO request = mock(PazienteUtenteDTO.class);
    when(request.getId()).thenReturn(pazId);
    when(request.getIdMedico()).thenReturn(medicoId);

    Paziente paziente = mock(Paziente.class);
    when(pazienteRepository.findById(pazId)).thenReturn(Optional.of(paziente));
    when(utenteRepository.findById(medicoId)).thenReturn(Optional.empty()); // triggers ResourceNotFound -> caught ->
                                                                            // BusinessException

    BusinessException ex = assertThrows(BusinessException.class, () -> service.update(request));
    assertTrue(ex.getMessage().contains("Failed to retrieve patients for medico"));
  }

  @Test
  void update_passwordNull_doesNotCallEncoder() {
    UUID pazId = UUID.randomUUID();
    PazienteUtenteDTO request = mock(PazienteUtenteDTO.class);
    when(request.getId()).thenReturn(pazId);
    when(request.getPasswordHash()).thenReturn(null);

    Paziente paziente = mock(Paziente.class);
    when(pazienteRepository.findById(pazId)).thenReturn(Optional.of(paziente));

    ResponseEntity<String> resp = service.update(request);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
    verify(passwordEncoder, never()).encode(anyString());
    verify(pazienteRepository).save(paziente);
  }

}
