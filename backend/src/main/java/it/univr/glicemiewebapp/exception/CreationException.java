
package it.univr.glicemiewebapp.exception;

public class CreationException extends BusinessException {
  public CreationException(String message) {
    super("CREATION_ERROR", message);

  }
}
