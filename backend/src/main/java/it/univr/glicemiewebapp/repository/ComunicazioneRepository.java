package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Comunicazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
pubblic interface ComunicazioneRepository extends JpaRepository<Comunicazione, UUID>{
}