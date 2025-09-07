package it.univr.glicemiewebapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sintomi")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@ToString

public class Sintomo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_sintomo", nullable = false)
    private UUID id;

    @Size(max = 20)
    @NotNull
    @Column(name = "nome", nullable = false, length = 20)
    private String nome;

    @NotNull
    @Column(name = "descrizione", nullable = false, length = Integer.MAX_VALUE)
    private String descrizione;

    @OneToMany(mappedBy = "idSintomo", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Segnalazione> segnalazioni = new ArrayList<>();


}