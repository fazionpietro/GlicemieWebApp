package it.univr.glicemiewebapp.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.univr.glicemiewebapp.dto.AlertTerapia;
import it.univr.glicemiewebapp.dto.TerapiaDTO;
import it.univr.glicemiewebapp.entity.Terapia;
import it.univr.glicemiewebapp.entity.Utente;
import it.univr.glicemiewebapp.entity.Paziente;

public interface TerapiaRepository extends JpaRepository<Terapia, UUID> {

    @Query("""
        SELECT new it.univr.glicemiewebapp.dto.TerapiaDTO(
            t.id,
            t.farmaco,
            t.numAssunzioni,
            t.dosaggio,
            t.indicazioni,
            t.idPaziente.id,
            t.medicoCurante.id
        )
        FROM Terapia t
        WHERE t.medicoCurante = :medicoCurante
        """)
    List<TerapiaDTO> findByMedicoCurante(@Param("medicoCurante") Utente medicoCurante);

    @Query("""
        SELECT new it.univr.glicemiewebapp.dto.AlertTerapia(
            t.farmaco,
            p.nome,
            p.cognome,
            p.email,
            t.id,
            p.id
        )
        FROM Terapia t
        JOIN t.idPaziente p
        WHERE NOT EXISTS (
            SELECT 1 FROM Assunzione a
            WHERE a.idTerapia = t
            AND a.timestamp >= :treGiorniFa
        )
        ORDER BY p.cognome, p.nome, t.farmaco
        """)
        
    List<AlertTerapia> findPazientiInadempienti(@Param("treGiorniFa") Instant treGiorniFa);

    @Query("""
            SELECT new it.univr.glicemiewebapp.dto.TerapiaDTO(
            t.id, t.farmaco, t.numAssunzioni, t.dosaggio, t.indicazioni, t.idPaziente.id, t.medicoCurante.id)
            FROM Terapia t WHERE t.idPaziente = :paziente
            """)
    List<TerapiaDTO> findByPaziente(@Param("paziente") Paziente paziente);
}
