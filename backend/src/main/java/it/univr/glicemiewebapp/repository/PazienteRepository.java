package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Paziente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PazienteRepository extends JpaRepository<Paziente, UUID> {
}
