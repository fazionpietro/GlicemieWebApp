package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.dto.AssunzionePazienteDTO;
import it.univr.glicemiewebapp.entity.Assunzione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AssunzioneRepository extends JpaRepository<Assunzione, UUID> {
  @Query("""
      SELECT new it.univr.glicemiewebapp.dto.AssunzionePazienteDTO(
          t.id,
          t.id,
          (SELECT MAX(a2.timestamp) FROM Assunzione a2 WHERE a2.idTerapia.id = t.id),
          (SELECT COUNT(a3) FROM Assunzione a3 WHERE a3.idTerapia.id = t.id
           AND a3.timestamp >= :day)
      )
      FROM Terapia t
      WHERE t.idPaziente.id = :pazienteId
      """)
  List<AssunzionePazienteDTO> findAssunzioniByPazienteId(@Param("pazienteId") UUID pazienteId,
      @Param("day") Instant day);
}
