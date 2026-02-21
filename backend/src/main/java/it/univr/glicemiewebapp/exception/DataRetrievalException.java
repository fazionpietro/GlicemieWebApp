package it.univr.glicemiewebapp.exception;

public class DataRetrievalException extends BusinessException {
  public DataRetrievalException(String message) {
    super("DATA_RETRIEVAL_ERROR", message);
  }

  public DataRetrievalException(String message, Throwable cause) {
    super("DATA_RETRIEVAL_ERROR", message, cause);
  }
}
