package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.dto.TerapiaDTO;
import it.univr.glicemiewebapp.entity.Terapia;
import it.univr.glicemiewebapp.entity.Utente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

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
}
