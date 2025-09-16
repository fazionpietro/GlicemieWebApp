package it.univr.glicemiewebapp.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import it.univr.glicemiewebapp.service.LogService;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @Autowired
  private final LogService log;

  public GlobalExceptionHandler(LogService log) {
    this.log = log;
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    log.error("Resource not found: " + ex.getMessage());
    ErrorResponse error = ErrorResponse.of(ex.getErrorCode(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
    log.error("Validation error: " + ex.getMessage());
    ErrorResponse error = ErrorResponse.of(ex.getErrorCode(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(DataRetrievalException.class)
  public ResponseEntity<ErrorResponse> handleDataRetrieval(DataRetrievalException ex) {
    log.error("Data retrieval error: " + ex.getMessage() + " " + ex);
    ErrorResponse error = ErrorResponse.of(ex.getErrorCode(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(UpdateException.class)
  public ResponseEntity<ErrorResponse> handleUpdate(UpdateException ex) {
    log.error("Update error: " + ex.getMessage() + " " + ex);
    ErrorResponse error = ErrorResponse.of(ex.getErrorCode(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
    log.error("Business error: " + ex.getMessage() + " " + ex);
    ErrorResponse error = ErrorResponse.of(ex.getErrorCode(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(DeletionException.class)
  public ResponseEntity<ErrorResponse> handleDeletion(DeletionException ex) {
    log.error("Deletion error: " + ex.getMessage() + " ex");
    ErrorResponse error = ErrorResponse.of(ex.getErrorCode(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(CreationException.class)
  public ResponseEntity<ErrorResponse> handleDeletion(CreationException ex) {
    log.error("Creation error: " + ex.getMessage() + " ex");
    ErrorResponse error = ErrorResponse.of(ex.getErrorCode(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
    log.error("Unexpected error occurred" + ex);
    ErrorResponse error = ErrorResponse.of("INTERNAL_SERVER_ERROR", "An unexpected error occurred");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
