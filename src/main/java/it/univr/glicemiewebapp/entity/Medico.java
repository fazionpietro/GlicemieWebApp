package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medici")
@PrimaryKeyJoinColumn(name = "id_medico")
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@ToString

public class Medico extends Utente {

    @OneToMany(mappedBy = "medicoCurante", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Terapia> terapiePrescritte = new ArrayList<>();

    public Medico(String email, String passwordHash, String nome, String cognome) {
        super(null, email, passwordHash, nome, cognome);
    }
}




