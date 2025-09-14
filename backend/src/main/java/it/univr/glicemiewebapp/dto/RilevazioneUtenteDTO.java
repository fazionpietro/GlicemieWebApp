package it.univr.glicemiewebapp.dto;

import it.univr.glicemiawebapp.dto;
import it.univr.glicemiewebapp.entity.Rilevazione;
import java.time.Instant;
import java.util.UUID;


public class RilevazioneUtenteDTO {
    private String id;
    private double valore;
    private Instant timestamp;
    private String livello;

    public RilevazioneUtenteDTO(Rilevazione r){
        this.id=r.getId().toString();
        this.valore = r.getValore();
        this.timestamp= r.getTimestamp();
        this.livello = calcoloLivello(r.getValore());
    }

    private String calcoloLivello(double valore){
        if(valore<70){
            return "basso";
        }else if(valore > 180){
            return "alto";
        }else{
            return "normale";
        }
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getLivello() {
        return livello;
    }

    public void setLivello(String livello) {
        this.livello = livello;
    }
}