package it.univr.glicemiewebapp.entity;

@Entity
@Table(name = "comunicazioni")
@Data
@NoArgConstructor
@AllArgsConstructor

public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id_comunicazione")
    private UUID id;
 
    @Column(name="descrizione", length=Integer.MAX_VALUE)
    private String descrizione;

    @Column(name="priorita", length= Integer.MAX_VALUE)
    private String priorita;

    @column(name="timestamp")
    private LocalDateTime dataInvio;

    @Column(name = "id_paziente")
    private UUID idPaziente;

    @PrePersist
    protected void onCreate(){
        dataInvio=LocalDateTime.now();
    }

    public Message(String descrizione, String priorita, UUID idPaziente){
        this.descrizione = descrizione;
        this.priorita = priorita;
        this.idPaziente = idPaziente
    }
}
