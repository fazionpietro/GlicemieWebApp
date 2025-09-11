package it.univr.glicemiewebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "utenti")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Getter
@Setter
@ToString(exclude = "passwordHash")

public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_utente", nullable = false)
    private UUID id;

    @Size(max = 320)
    @NotNull
    @Column(name = "email", nullable = false, length = 320, unique = true)
    private String email;

    @Size(max = 256)
    @NotNull
    @Column(name = "password_hash", nullable = false, length = 256)
    @JsonIgnore
    private String passwordHash;

    @Size(max = 20)
    @NotNull
    @Column(name = "nome", nullable = false, length = 20)
    private String nome;

    @Size(max = 20)
    @NotNull
    @Column(name = "cognome", nullable = false, length = 20)
    private String cognome;

    @NotNull
    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @NotNull
    @Column(name = "ruolo", nullable = false, length = 20)
    private String ruolo;


    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(this.ruolo));
    }

    

}