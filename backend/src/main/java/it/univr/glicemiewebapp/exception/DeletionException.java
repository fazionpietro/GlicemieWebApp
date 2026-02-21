package it.univr.glicemiewebapp.exception;

public class DeletionException extends BusinessException {
  public DeletionException(String message) {
    super("DELETION_ERROR", message);

  }
}
