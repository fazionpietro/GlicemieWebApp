
package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Entity for storing invalidated JWT tokens (e.g., after logout).
 * Used to prevent reuse of tokens even if they haven't expired yet.
 */
@Entity
@Table(name = "token_blacklist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistedToken {

    @Id
    private String jti; // unique identifier

    @Column(nullable = false)
    private Instant expiry;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}
