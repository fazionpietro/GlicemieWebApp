package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "farmaci")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@ToString

public class Farmaco {

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

    @OneToMany(mappedBy = "idFarmaco", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Assunzione> assunzioni = new ArrayList<>();

    @OneToMany(mappedBy = "idFarmaco", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Terapia> terapie = new ArrayList<>();

}