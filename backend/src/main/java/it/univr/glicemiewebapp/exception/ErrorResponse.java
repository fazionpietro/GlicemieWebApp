package it.univr.glicemiewebapp.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
  private String errorCode;
  private String message;
  private LocalDateTime timestamp;

  public ErrorResponse() {
  }

  public ErrorResponse(String errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }

  public static ErrorResponse of(String errorCode, String message) {
    return new ErrorResponse(errorCode, message);
  }

  // Getters and setters
  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }
}
