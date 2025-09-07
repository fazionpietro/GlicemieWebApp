package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Rilevazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RilevazioneRepository extends JpaRepository<Rilevazione, UUID> {
}
