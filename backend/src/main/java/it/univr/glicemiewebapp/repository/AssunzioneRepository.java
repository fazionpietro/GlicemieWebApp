package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Assunzione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssunzioneRepository extends JpaRepository<Assunzione, UUID> {
}
