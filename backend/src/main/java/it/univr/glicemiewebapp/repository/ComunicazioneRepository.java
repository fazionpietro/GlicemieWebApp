package it.univr.glicemiewebapp.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.univr.glicemiewebapp.dto.ComunicazioneMedicoDTO;
import it.univr.glicemiewebapp.entity.Comunicazione;

@Repository
public interface ComunicazioneRepository extends JpaRepository<Comunicazione, UUID> {
  @Query("""
      SELECT new it.univr.glicemiewebapp.dto.ComunicazioneMedicoDTO(
          c.id,
          c.priorita,
          c.descrizione,
          p.nome,
          p.cognome,
          p.email,
          c.timestamp
      ) FROM Comunicazione c
      LEFT JOIN c.idPaziente p
      WHERE p.idMedico.id = :id_medico and c.letto = FALSE
      """)
  List<ComunicazioneMedicoDTO> findByMedico(@Param("id_medico") UUID idMedico);
};
