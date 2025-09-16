package it.univr.glicemiewebapp.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AssunzionePazienteDTO {

  private final UUID id;
  private final UUID idTerapia;
  private final Instant latestTimestamp;
  private final int giaAssunte;

  public AssunzionePazienteDTO(UUID id, UUID idTerapia, Instant latestTimestamp, Long giaAssunte) {
    this.id = id;
    this.idTerapia = idTerapia;
    this.latestTimestamp = latestTimestamp;
    this.giaAssunte = giaAssunte != null ? giaAssunte.intValue() : 0;
  }
}
