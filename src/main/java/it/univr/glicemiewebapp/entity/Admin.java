package it.univr.glicemiewebapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "id_admin")
@NoArgsConstructor

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@ToString
public class Admin extends Utente {



    public Admin(String email, String passwordHash, String nome, String cognome) {
        super(null, email, passwordHash, nome, cognome, "ROLE_ADMIN");
    }


}