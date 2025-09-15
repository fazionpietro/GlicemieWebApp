package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Rilevazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.util.List;

public interface RilevazioneRepository extends JpaRepository<Rilevazione, UUID> {
  List<Rilevazione> findByIdPaziente_Id(UUID idPaziente);

  @Query("SELECT r FROM Rilevazione r " +
      "JOIN r.idPaziente p " +
      "WHERE p.idMedico.id = :medicoId")
  List<Rilevazione> findAllByPazienteIdMedicoId(@Param("medicoId") UUID medicoId);

}
