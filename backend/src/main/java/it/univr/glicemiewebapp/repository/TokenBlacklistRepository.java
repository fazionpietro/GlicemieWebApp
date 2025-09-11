package it.univr.glicemiewebapp.repository;

import it.univr.glicemiewebapp.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

public interface TokenBlacklistRepository extends JpaRepository<BlacklistedToken, String> {

    @Modifying
    @Query("delete from BlacklistedToken t where t.expiry < ?1")
    void deleteAllExpiredSince(Instant instant);
}