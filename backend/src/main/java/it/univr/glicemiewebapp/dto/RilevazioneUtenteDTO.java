import java.time.LocalDateTime;


public class RilevazioneUtenteDTO {
    private String id;
    private double valore;
    private LocalDateTime timestamp;
    private String livello;

    public RilevazioneUtenteDTO(Rilevazione r){
        this.id=r.getId();
        this.valore = r.getValore();
        this.timestamp= r.getTimestamp();
    }

public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValore() {
        return valore;
    }

    public void setValore(double valore) {
        this.valore = valore;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getLivello() {
        return livello;
    }

    public void setLivello(String livello) {
        this.livello = livello;
    }
}