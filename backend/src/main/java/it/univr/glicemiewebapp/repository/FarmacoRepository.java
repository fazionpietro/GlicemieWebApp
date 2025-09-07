package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Farmaco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FarmacoRepository extends JpaRepository<Farmaco, UUID> {
}
