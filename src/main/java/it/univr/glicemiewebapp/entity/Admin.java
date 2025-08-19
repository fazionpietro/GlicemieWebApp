package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @Column(name = "id_admin", nullable = false)
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_admin", nullable = false)
    private Utenti utenti;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Utenti getUtenti() {
        return utenti;
    }

    public void setUtenti(Utenti utenti) {
        this.utenti = utenti;
    }

}