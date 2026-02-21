package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

/**
 * Repository for managing blacklisted JWT tokens.
 */
public interface TokenBlacklistRepository extends JpaRepository<BlacklistedToken, String> {

    /**
     * Deletes all tokens that have expired before a given time.
     * 
     * @param instant The cutoff time
     */
    @Modifying
    @Query("delete from BlacklistedToken t where t.expiry < ?1")
    void deleteAllExpiredSince(Instant instant);
}