package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "farmaci")
public class Farmaci {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_farmaco", nullable = false)
    private UUID id;

    @Size(max = 20)
    @NotNull
    @Column(name = "nome", nullable = false, length = 20)
    private String nome;

    @Column(name = "bugiardino", length = Integer.MAX_VALUE)
    private String bugiardino;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBugiardino() {
        return bugiardino;
    }

    public void setBugiardino(String bugiardino) {
        this.bugiardino = bugiardino;
    }

}