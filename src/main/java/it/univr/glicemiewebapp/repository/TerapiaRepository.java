package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Terapia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TerapiaRepository extends JpaRepository<Terapia, UUID> {
}
