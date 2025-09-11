package it.univr.glicemiewebapp.forms;

import java.util.UUID;

public class DeleteRequest {
    private UUID id;

    public DeleteRequest(UUID id){
        this.id=id;
    }

    public UUID getId(){
        return this.id;
    }
}
