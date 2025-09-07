package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Sintomo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SintomoRepository extends JpaRepository<Sintomo, UUID> {
}
