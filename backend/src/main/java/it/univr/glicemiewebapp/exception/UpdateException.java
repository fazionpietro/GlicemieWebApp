package it.univr.glicemiewebapp.exception;

public class UpdateException extends BusinessException {
  public UpdateException(String message) {
    super("UPDATE_ERROR", message);
  }

  public UpdateException(String message, Throwable cause) {
    super("UPDATE_ERROR", message, cause);
  }
}
