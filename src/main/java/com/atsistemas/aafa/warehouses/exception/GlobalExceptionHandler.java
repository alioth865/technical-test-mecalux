package com.atsistemas.aafa.warehouses.exception;

import com.atsistemas.warehouses.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = NotFoundException.class)
  public ResponseEntity<ErrorResponse> notFoundException(NotFoundException notFoundException) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setCode(notFoundException.getCode());
    errorResponse.setDetail(notFoundException.getDetail());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = UnsupportedRackOnFamily.class)
  public ResponseEntity<ErrorResponse> unsupportedRackOnFamily(
      UnsupportedRackOnFamily unsupportedRackOnFamily) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setCode(unsupportedRackOnFamily.getCode());
    errorResponse.setDetail(unsupportedRackOnFamily.getDetail());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

}


