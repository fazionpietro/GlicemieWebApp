package it.univr.glicemiewebapp.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Risposta standardizzata per gli errori applicativi. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  private String errorCode;
  private String message;
  private LocalDateTime timestamp;

  public ErrorResponse(String errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }

  public static ErrorResponse of(String errorCode, String message) {
    return new ErrorResponse(errorCode, message);
  }
}
