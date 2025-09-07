package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Segnalazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SegnalazioneRepository extends JpaRepository<Segnalazione, UUID> {
}
