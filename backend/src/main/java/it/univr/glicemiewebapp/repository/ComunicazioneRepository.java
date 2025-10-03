package it.univr.glicemiewebapp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.univr.glicemiewebapp.entity.Comunicazione;

@Repository
public interface ComunicazioneRepository extends JpaRepository<Comunicazione, UUID>{
    
};