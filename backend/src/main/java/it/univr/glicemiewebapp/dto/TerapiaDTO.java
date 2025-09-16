package it.univr.glicemiewebapp.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TerapiaDTO {

  private UUID id;
  private String farmaco;
  private int numAssunzioni;
  private String dosaggio;
  private String indicazioni;
  private UUID idPaziente;
  private UUID idMedico;

}
