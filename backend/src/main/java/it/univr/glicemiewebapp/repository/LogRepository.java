package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Log;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.UUID;

public interface LogRepository extends JpaRepositoryImplementation<Log, UUID> {
}
