package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.dto.UtenteDTO;
import it.univr.glicemiewebapp.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for accessing Utente entities.
 */
@Repository
public interface UtenteRepository extends JpaRepository<Utente, UUID> {

  /**
   * Finds a user by their email address.
   * 
   * @param email The email to search for
   * @return Optional containing the user if found
   */
  @Query("SELECT u FROM Utente u WHERE u.email = ?1")
  Optional<Utente> findByEmailAddress(String email);

  @NativeQuery("SELECT COUNT(RUOLO) AS num FROM UTENTI WHERE RUOLO = ?1")
  String count(String ruolo);

  boolean existsByEmail(String email);

  @Query("""
      SELECT new it.univr.glicemiewebapp.dto.UtenteDTO(
          u.id,
          u.email,
          u.nome,
          u.cognome,
          u.dataNascita
      )
      FROM Utente u
      WHERE u.ruolo = ?1
      """)
  List<UtenteDTO> findByRole(String ruolo);

}
