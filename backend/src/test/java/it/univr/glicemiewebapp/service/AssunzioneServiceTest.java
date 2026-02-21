package it.univr.glicemiewebapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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

import it.univr.glicemiewebapp.dto.AssunzioneDTO;
import it.univr.glicemiewebapp.dto.AssunzionePazienteDTO;
import it.univr.glicemiewebapp.entity.Assunzione;
import it.univr.glicemiewebapp.entity.Terapia;
import it.univr.glicemiewebapp.exception.BusinessException;
import it.univr.glicemiewebapp.repository.AssunzioneRepository;
import it.univr.glicemiewebapp.repository.TerapiaRepository;

@ExtendWith(MockitoExtension.class)
class AssunzioneServiceTest {

    @Mock
    private AssunzioneRepository repository;

    @Mock
    private TerapiaRepository terapiaRepository;

    @InjectMocks
    private AssunzioneService assunzioneService;

    private AssunzioneDTO assunzioneDTO;
    private Terapia terapia;
    private UUID terapiaId;
    private UUID pazienteId;
    private UUID assunzioneId;

    @BeforeEach
    void setUp() {
        terapiaId = UUID.randomUUID();
        pazienteId = UUID.randomUUID();
        assunzioneId = UUID.randomUUID();

        terapia = new Terapia();
        terapia.setId(terapiaId);
        assunzioneDTO = new AssunzioneDTO(assunzioneId, terapiaId);
    }

    @Test
    void create_Success() {
        // Given
        when(terapiaRepository.findById(terapiaId)).thenReturn(Optional.of(terapia));
        when(repository.save(any(Assunzione.class))).thenReturn(new Assunzione());
        List<AssunzioneDTO> request = Arrays.asList(assunzioneDTO);

        // When
        String result = assunzioneService.create(request);

        // Then
        assertEquals("Assumption stored correctly", result);
        verify(terapiaRepository).findById(terapiaId);
        verify(repository).save(any(Assunzione.class));
    }

    @Test
    void create_TerapiaNotFound() {
        // Given
        when(terapiaRepository.findById(terapiaId)).thenReturn(Optional.empty());
        List<AssunzioneDTO> request = Arrays.asList(assunzioneDTO);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> assunzioneService.create(request));
        assertEquals("CREATION_ERROR", exception.getErrorCode());
    }

    @Test
    void getAlreadyTaken_Exception() {
        // Given
        when(repository.findAssunzioniByPazienteId(any(UUID.class), any(Instant.class)))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> assunzioneService.getAlreadyTaken(pazienteId));
        assertEquals("DATA_RETRIEVAL_ERROR", exception.getErrorCode());
    }
}
