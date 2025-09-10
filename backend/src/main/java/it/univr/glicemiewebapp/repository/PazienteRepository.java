package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.dto.PazienteUtenteDTO;
import it.univr.glicemiewebapp.entity.Paziente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PazienteRepository extends JpaRepository<Paziente, UUID> {

    @Query("""
    SELECT new it.univr.glicemiewebapp.dto.PazienteUtenteDTO(
        p.id, p.email, p.nome, p.cognome, p.dataNascita, p.ruolo,
        p.fattoriRischio, p.comorbita, p.patologiePregresse,
        p.idMedico.id
    )
    FROM Paziente p
    LEFT JOIN p.idMedico
    """)
    List<PazienteUtenteDTO> findAllComplete();
}