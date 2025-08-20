package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicoRepository extends JpaRepository<Medico, UUID> {
}
