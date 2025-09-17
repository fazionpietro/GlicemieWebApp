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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import it.univr.glicemiewebapp.dto.TerapiaDTO;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.entity.Terapia;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.exception.BusinessException;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.repository.TerapiaRepository;
import it.univr.glicemiewebapp.repository.UtenteRepository;

@ExtendWith(MockitoExtension.class)
class TerapiaServiceTest {

  @Mock
  private TerapiaRepository terapiaRepository;

  @Mock
  private UtenteRepository utenteRepository;

  @Mock
  private PazienteRepository pazienteRepository;

  @InjectMocks
  private TerapiaService terapiaService;

  private TerapiaDTO terapiaDTO;
  private Terapia terapia;
  private Utente medico;
  private Paziente paziente;
  private UUID terapiaId;
  private UUID medicoId;
  private UUID pazienteId;

  @BeforeEach
  void setUp() {
    terapiaId = UUID.randomUUID();
    medicoId = UUID.randomUUID();
    pazienteId = UUID.randomUUID();

    terapiaDTO = new TerapiaDTO();
    terapiaDTO.setId(terapiaId);
    terapiaDTO.setIdMedico(medicoId);
    terapiaDTO.setIdPaziente(pazienteId);
    terapiaDTO.setFarmaco("Metformina");
    terapiaDTO.setNumAssunzioni(2);
    terapiaDTO.setDosaggio("500mg");
    terapiaDTO.setIndicazioni("Assumere durante i pasti");

    medico = new Utente();
    medico.setId(medicoId);

    paziente = new Paziente();
    paziente.setId(pazienteId);

    terapia = new Terapia();
    terapia.setId(terapiaId);
    terapia.setFarmaco("Metformina");
  }

  @Test
  void create_Success() {
    // Given
    when(pazienteRepository.findById(pazienteId)).thenReturn(Optional.of(paziente));
    when(utenteRepository.findById(medicoId)).thenReturn(Optional.of(medico));
    when(terapiaRepository.save(any(Terapia.class))).thenReturn(terapia);

    // When
    ResponseEntity<String> result = terapiaService.create(terapiaDTO);

    // Then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("TERAPIA CREATA", result.getBody());
    verify(terapiaRepository).save(any(Terapia.class));
  }

  @Test
  void create_Exception() {
    // Given
    when(pazienteRepository.findById(pazienteId)).thenThrow(new RuntimeException("Database error"));

    // When & Then
    BusinessException exception = assertThrows(BusinessException.class,
        () -> terapiaService.create(terapiaDTO));
    assertEquals("CREATION_ERROR", exception.getErrorCode());
  }

  @Test
  void getAllByMedico_Success() {
    // Given
    List<TerapiaDTO> expectedList = Arrays.asList(terapiaDTO);
    when(utenteRepository.findById(medicoId)).thenReturn(Optional.of(medico));
    when(terapiaRepository.findByMedicoCurante(medico)).thenReturn(expectedList);

    // When
    List<TerapiaDTO> result = terapiaService.getAllByMedico(medicoId);

    // Then
    assertEquals(expectedList, result);
    verify(terapiaRepository).findByMedicoCurante(medico);
  }

  @Test
  void getAllByMedico_Exception() {
    // Given
    when(utenteRepository.findById(medicoId)).thenThrow(new RuntimeException("Database error"));

    // When & Then
    BusinessException exception = assertThrows(BusinessException.class,
        () -> terapiaService.getAllByMedico(medicoId));
    assertEquals("DATA_RETRIEVAL_ERROR", exception.getErrorCode());
  }

  @Test
  void delete_Success() {
    // When
    ResponseEntity<String> result = terapiaService.delete(terapiaId);

    // Then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("TERAPIA ELIMINATA", result.getBody());
    verify(terapiaRepository).deleteById(terapiaId);
  }

  @Test
  void delete_Exception() {
    // Given
    doThrow(new RuntimeException("Database error")).when(terapiaRepository).deleteById(terapiaId);

    // When & Then
    BusinessException exception = assertThrows(BusinessException.class,
        () -> terapiaService.delete(terapiaId));
    assertEquals("DELETION_ERROR", exception.getErrorCode());
  }

  @Test
  void update_Success() {
    // Given
    when(terapiaRepository.findById(terapiaId)).thenReturn(Optional.of(terapia));
    when(terapiaRepository.save(any(Terapia.class))).thenReturn(terapia);

    // When
    ResponseEntity<String> result = terapiaService.update(terapiaDTO, terapiaId);

    // Then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("TERAPIA AGGIORNATA", result.getBody());
    verify(terapiaRepository).save(terapia);
  }

  @Test
  void update_NotFound() {
    // Given
    when(terapiaRepository.findById(terapiaId)).thenReturn(Optional.empty());

    // When & Then
    BusinessException exception = assertThrows(BusinessException.class,
        () -> terapiaService.update(terapiaDTO, terapiaId));
    assertEquals("UPDATE_ERROR", exception.getErrorCode());
  }
}
