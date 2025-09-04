package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, UUID> {

    @Query("SELECT u FROM Utente u WHERE u.email = ?1")
    Optional<Utente> findByEmailAddress(String email);

}

