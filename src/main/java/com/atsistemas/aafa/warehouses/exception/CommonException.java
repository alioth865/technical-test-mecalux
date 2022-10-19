package com.atsistemas.aafa.warehouses.exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

  private final String code;
  private final String detail;

  public CommonException(String code, String detail) {
    super();
    this.code = code;
    this.detail = detail;
  }
}
