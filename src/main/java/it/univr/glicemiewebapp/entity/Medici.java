package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "medici")
public class Medici {
    @Id
    @Column(name = "id_medico", nullable = false)
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_medico", nullable = false)
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