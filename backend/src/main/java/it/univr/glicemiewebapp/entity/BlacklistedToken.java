

package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "token_blacklist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistedToken {

    @Id
    private String jti;          // JWT ID (claim “jti”) → univoco per token

    @Column(nullable = false)
    private Instant expiry;      // quando scade (per poterlo cancellare)

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}