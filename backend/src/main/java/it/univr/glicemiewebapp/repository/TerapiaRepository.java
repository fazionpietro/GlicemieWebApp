package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Terapia;
import it.univr.glicemiewebapp.entity.Utente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TerapiaRepository extends JpaRepository<Terapia, UUID> {

  List<Terapia> findByMedicoCurante(Utente medicoCurante);
}
