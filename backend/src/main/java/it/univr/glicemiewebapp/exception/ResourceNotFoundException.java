package it.univr.glicemiewebapp.exception;

public class ResourceNotFoundException extends BusinessException {
  public ResourceNotFoundException(String resource, Object id) {
    super("RESOURCE_NOT_FOUND", String.format("%s with id %s not found", resource, id));
  }
}
