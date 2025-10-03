package it.univr.glicemiewebapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import it.univr.glicemiewebapp.dto.RilevazioneUtenteDTO;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.entity.Rilevazione;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.repository.PazienteRepository;
import it.univr.glicemiewebapp.repository.RilevazioneRepository;
import it.univr.glicemiewebapp.repository.UtenteRepository;

@ExtendWith(MockitoExtension.class)
class RilevazioneServiceTest {

  @Mock
  private RilevazioneRepository rilevazioni;

  @Mock
  private PazienteRepository pazienti;

  @Mock
  private UtenteRepository utenti;

  @Mock
  private ObjectMapper mapper;

  @Mock
  private LogService log;

  @InjectMocks
  private RilevazioneService rilevazioneService;

  private UUID pazienteId;
  private UUID utenteId;
  private Paziente paziente;
  private Utente utente;
  private Rilevazione rilevazione;

  @BeforeEach
  void setUp() {
    pazienteId = UUID.randomUUID();
    utenteId = UUID.randomUUID();

    paziente = new Paziente();
    paziente.setId(pazienteId);

    utente = new Utente();
    utente.setId(utenteId);

    rilevazione = Rilevazione.builder()
        .id(UUID.randomUUID())
        .idPaziente(paziente)
        .valore(120.0)
        .timestamp(Instant.now())
        .build();
  }

  @Test
  void addRilevazione_ok() {
    Double valore = 120.0;
    when(pazienti.findById(pazienteId)).thenReturn(Optional.of(paziente));
    when(rilevazioni.save(any(Rilevazione.class))).thenReturn(rilevazione);

    Rilevazione result = rilevazioneService.addRilevazione(pazienteId, valore);

    assertNotNull(result);
    verify(pazienti).findById(pazienteId);
    verify(rilevazioni).save(any(Rilevazione.class));
    verify(log).info(contains("hai inserito una rilevazione"));
  }

  @Test
  void addRilevazione_PazienteNotFound() {
    // Given
    Double valore = 120.0;
    when(pazienti.findById(pazienteId)).thenReturn(Optional.empty());

    // When & Then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> rilevazioneService.addRilevazione(pazienteId, valore));
    assertEquals("Paziente non trovato", exception.getMessage());
  }

  @Test
  void getAllRilevazioni_Success() {
    // Given
    List<Rilevazione> expectedList = Arrays.asList(rilevazione);
    when(rilevazioni.findAll()).thenReturn(expectedList);

    // When
    List<Rilevazione> result = rilevazioneService.getAllRilevazioni();

    // Then
    assertEquals(expectedList, result);
    verify(rilevazioni).findAll();
  }

  @Test
  void getByPaziente_Success() {
    // Given
    List<Rilevazione> expectedList = Arrays.asList(rilevazione);
    when(rilevazioni.findByIdPaziente_Id(pazienteId)).thenReturn(expectedList);

    // When
    List<Rilevazione> result = rilevazioneService.getByPaziente(pazienteId);

    // Then
    assertEquals(expectedList, result);
    verify(rilevazioni).findByIdPaziente_Id(pazienteId);
  }

  @Test
  void deleteRilevazione_Success() {
    // Given
    UUID rilevazioneId = UUID.randomUUID();

    // When
    rilevazioneService.deleteRilevazione(rilevazioneId);

    // Then
    verify(rilevazioni).deleteById(rilevazioneId);
  }

  @Test
  void getByPazienteDTO_Success() {
    // Given
    List<Rilevazione> rilevazioni = Arrays.asList(rilevazione);
    when(this.rilevazioni.findByIdPaziente_Id(pazienteId)).thenReturn(rilevazioni);

    // When
    List<RilevazioneUtenteDTO> result = rilevazioneService.getByPazienteDTO(pazienteId);

    // Then
    assertEquals(1, result.size());
    verify(this.rilevazioni).findByIdPaziente_Id(pazienteId);
  }

  @Test
  void getAllMyRilevazioni_Admin_Success() throws Exception {
    // Given
    utente.setRuolo("ROLE_ADMIN");
    List<Rilevazione> allRilevazioni = Arrays.asList(rilevazione);

    when(utenti.findById(utenteId)).thenReturn(Optional.of(utente));
    when(rilevazioni.findAll()).thenReturn(allRilevazioni);
    when(mapper.writeValueAsString(allRilevazioni)).thenReturn("[]");

    // When
    ResponseEntity<String> result = rilevazioneService.getAllMyRilevazioni(utenteId);

    // Then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("[]", result.getBody());
    verify(rilevazioni).findAll();
  }

  @Test
  void getAllMyRilevazioni_Medico_Success() throws Exception {
    // Given
    utente.setRuolo("ROLE_MEDICO");
    List<Rilevazione> medicoRilevazioni = Arrays.asList(rilevazione);

    when(utenti.findById(utenteId)).thenReturn(Optional.of(utente));
    when(rilevazioni.findAllByPazienteIdMedicoId(utenteId)).thenReturn(medicoRilevazioni);
    when(mapper.writeValueAsString(medicoRilevazioni)).thenReturn("[]");

    // When
    ResponseEntity<String> result = rilevazioneService.getAllMyRilevazioni(utenteId);

    // Then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("[]", result.getBody());
    verify(rilevazioni).findAllByPazienteIdMedicoId(utenteId);
  }

  @Test
  void getAllMyRilevazioni_InvalidRole() {
    // Given
    utente.setRuolo("ROLE_PAZIENTE");
    when(utenti.findById(utenteId)).thenReturn(Optional.of(utente));

    // When
    ResponseEntity<String> result = rilevazioneService.getAllMyRilevazioni(utenteId);

    // Then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    assertEquals("PERMISSION ERROR", result.getBody());
  }
}
