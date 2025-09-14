package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, UUID> {

  @Query("SELECT u FROM Utente u WHERE u.email = ?1 ")
  Optional<Utente> findByEmailAddress(String email);

  @Query("SELECT u FROM Utente u WHERE u.ruolo = ?1")
  List<Utente> findByRole(String role);

  @NativeQuery("SELECT COUNT(RUOLO) AS num FROM UTENTI WHERE RUOLO = ?1")
  String count(String ruolo);

}
