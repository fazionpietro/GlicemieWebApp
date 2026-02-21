package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.Log;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.UUID;

/**
 * Repository for accessing Log entities.
 * Extends JpaRepositoryImplementation to support standard CRUD and more.
 */
public interface LogRepository extends JpaRepositoryImplementation<Log, UUID> {
}
