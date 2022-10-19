package com.atsistemas.aafa.warehouses.exception;

import lombok.Getter;

@Getter
public enum ErrorEnum {

  NOT_FOUND_RACK("Rack id [%s] not found.", "NF-001"),
  NOT_FOUND_WAREHOUSE("Warehouse id [%s] not found.", "NF-002"),
  UNKNOWN_FAMILY("Unknown family [%s].", "NF-003"),
  UNSUPPORTED_RACK_ON_FAMILY("Rack type [%s] not supported on family [%s].", "NF-004");

  private final String detail;
  private final String code;

  ErrorEnum(String detail, String code) {
    this.detail = detail;
    this.code = code;
  }


}
