package it.univr.glicemiewebapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import it.univr.glicemiewebapp.dto.ComunicazioneDTO;
import it.univr.glicemiewebapp.dto.ComunicazioneMedicoDTO;
import it.univr.glicemiewebapp.entity.Comunicazione;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.entity.Utente; // Usiamo Utente come tipo per il medico
import it.univr.glicemiewebapp.event.NewComunicazioneEvent;
import it.univr.glicemiewebapp.exception.BusinessException;
import it.univr.glicemiewebapp.repository.ComunicazioneRepository;
import it.univr.glicemiewebapp.repository.PazienteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ComunicazioneServiceTest {

  @Mock
  private ComunicazioneRepository comunicazioneRepository;

  @Mock
  private PazienteRepository pazienteRepository;

  @Mock
  private ApplicationEventPublisher publisher;

  @InjectMocks
  private ComunicazioneService comunicazioneService;

  private UUID pazienteId;
  private UUID medicoId;
  private UUID comunicazioneId;
  private Paziente paziente;
  private Utente medico;
  private ComunicazioneDTO comunicazioneDTO;
  private Comunicazione comunicazione;

  static class StubNewComunicazioneEvent {
    private final UUID medicoId;
    private final ComunicazioneMedicoDTO dto;

    public StubNewComunicazioneEvent(UUID medicoId, ComunicazioneMedicoDTO dto) {
      this.medicoId = medicoId;
      this.dto = dto;
    }

    public UUID getMedicoId() {
      return medicoId;
    }

    public ComunicazioneMedicoDTO getDto() {
      return dto;
    }
  }

  @BeforeEach
  void setUp() {
    pazienteId = UUID.randomUUID();
    medicoId = UUID.randomUUID();
    comunicazioneId = UUID.randomUUID();

    medico = Utente.builder()
        .id(medicoId)
        .nome("Dottor")
        .cognome("Bianchi")
        .email("doc@example.com")
        .ruolo("ROLE_MEDICO")
        .dataNascita(LocalDate.now().minusYears(45))
        .build();

    paziente = new Paziente();
    paziente.setId(pazienteId);
    paziente.setNome("Mario");
    paziente.setCognome("Rossi");
    paziente.setEmail("mario.rossi@example.com");
    paziente.setIdMedico(medico); // Imposta l'oggetto Utente
    paziente.setDataNascita(LocalDate.now().minusYears(30));
    paziente.setRuolo("ROLE_PAZIENTE");

    comunicazioneDTO = ComunicazioneDTO.builder()
        .idPaziente(pazienteId)
        .priorita(1)
        .descrizione("Test descrizione")
        .build();

    comunicazione = Comunicazione.builder()
        .id(comunicazioneId)
        .idPaziente(paziente)
        .priorita(comunicazioneDTO.getPriorita())
        .descrizione(comunicazioneDTO.getDescrizione())
        .timestamp(Instant.now())
        .letto(false)
        .build();
  }

  @Test
  void testSalvaComunicazione_Success() {
    when(pazienteRepository.findById(pazienteId)).thenReturn(Optional.of(paziente));
    when(comunicazioneRepository.save(any(Comunicazione.class))).thenReturn(comunicazione);

    ArgumentCaptor<NewComunicazioneEvent> eventCaptor = ArgumentCaptor.forClass(NewComunicazioneEvent.class);

    Comunicazione result = comunicazioneService.salvaComunicazione(comunicazioneDTO);

    assertNotNull(result);
    assertEquals(comunicazioneId, result.getId());
    assertEquals(paziente, result.getIdPaziente());

    verify(pazienteRepository, times(1)).findById(pazienteId);
    verify(comunicazioneRepository, times(1)).save(any(Comunicazione.class));

    verify(publisher, times(1)).publishEvent(eventCaptor.capture());

  }

  @Test
  void testSalvaComunicazione_PazienteNotFound() {
    when(pazienteRepository.findById(pazienteId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      comunicazioneService.salvaComunicazione(comunicazioneDTO);
    });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertTrue(exception.getReason().contains("Paziente non trovato"));

    verify(comunicazioneRepository, never()).save(any());
    verify(publisher, never()).publishEvent(any());
  }

  @Test
  void testSalvaComunicazione_InternalError() {
    when(pazienteRepository.findById(pazienteId)).thenReturn(Optional.of(paziente));
    when(comunicazioneRepository.save(any(Comunicazione.class))).thenThrow(new RuntimeException("DB Error"));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      comunicazioneService.salvaComunicazione(comunicazioneDTO);
    });

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    assertTrue(exception.getReason().contains("errore: DB Error"));

    verify(publisher, never()).publishEvent(any());
  }

  @Test
  void testGetByMedico_Success() {
    ComunicazioneMedicoDTO dto1 = ComunicazioneMedicoDTO.builder().id(UUID.randomUUID()).descrizione("Test 1").build();
    List<ComunicazioneMedicoDTO> expectedList = List.of(dto1);

    when(comunicazioneRepository.findByMedico(medicoId)).thenReturn(expectedList);

    List<ComunicazioneMedicoDTO> result = comunicazioneService.getByMedico(medicoId);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(expectedList, result);
    verify(comunicazioneRepository, times(1)).findByMedico(medicoId);
  }

  @Test
  void testGetByMedico_RepositoryError() {
    when(comunicazioneRepository.findByMedico(medicoId)).thenThrow(new RuntimeException("DB Query Failed"));

    BusinessException exception = assertThrows(BusinessException.class, () -> {
      comunicazioneService.getByMedico(medicoId);
    });

    assertEquals("DATA_RETRIEVAL_ERROR", exception.getErrorCode());
    assertEquals("Failed to retrieve communications", exception.getMessage());
  }

  @Test
  void testMarkAsRead_Success() {
    when(comunicazioneRepository.findById(comunicazioneId)).thenReturn(Optional.of(comunicazione));

    assertFalse(comunicazione.isLetto());

    String result = comunicazioneService.markAsRead(comunicazioneId);

    assertTrue(result.contains("marked as read"));
    assertTrue(comunicazione.isLetto());
    verify(comunicazioneRepository, times(1)).findById(comunicazioneId);
  }

  @Test
  void testMarkAsRead_NotFound() {
    when(comunicazioneRepository.findById(comunicazioneId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      comunicazioneService.markAsRead(comunicazioneId);
    });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertTrue(exception.getReason().contains("Communication not found"));
  }

  @Test
  void testMarkAsRead_UpdateError() {

    Comunicazione spyComunicazione = spy(comunicazione);

    when(comunicazioneRepository.findById(comunicazioneId)).thenReturn(Optional.of(spyComunicazione));

    doThrow(new RuntimeException("DB Error")).when(spyComunicazione).setLetto(true);

    BusinessException exception = assertThrows(BusinessException.class, () -> {
      comunicazioneService.markAsRead(comunicazioneId);
    });

    assertEquals("UPDATE_ERROR", exception.getErrorCode());
    assertEquals("failed to mark communication as read", exception.getMessage());

    verify(spyComunicazione, times(1)).setLetto(true);
  }
}
