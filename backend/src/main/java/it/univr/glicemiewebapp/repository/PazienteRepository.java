package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.dto.PazienteDTO;
import it.univr.glicemiewebapp.dto.PazienteUtenteDTO;
import it.univr.glicemiewebapp.entity.Paziente;
import it.univr.glicemiewebapp.entity.Utente;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.UUID;

/**
 * Repository for accessing Paziente entities.
 */
@Repository
public interface PazienteRepository extends JpaRepository<Paziente, UUID> {

    /**
     * Retrieves all patients with complete details, including their assigned
     * doctor.
     * 
     * @return List of PazienteUtenteDTO
     */
    @Query("""
                SELECT new it.univr.glicemiewebapp.dto.PazienteUtenteDTO(
                    p.id,
                    p.email,
                    p.nome,
                    p.cognome,
                    p.dataNascita,
                    p.ruolo,
                    p.fattoriRischio,
                    p.comorbita,
                    p.patologiePregresse,
                    m.id,
                    m.nome,
                    m.cognome,
                    m.email
                )
                FROM Paziente p
                LEFT JOIN p.idMedico m
            """)
    List<PazienteUtenteDTO> findAllComplete();

    @Query("""
            SELECT new it.univr.glicemiewebapp.dto.PazienteDTO(
                p.id,
                p.email,
                p.nome,
                p.cognome,
                p.dataNascita,
                p.fattoriRischio,
                p.comorbita,
                p.patologiePregresse,
                p.idMedico
            )
            FROM Paziente p
            WHERE p.idMedico = :idMedico


            """)
    List<PazienteDTO> findByIdMedico(@Param("idMedico") Utente medico);

}
