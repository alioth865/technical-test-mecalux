package com.atsistemas.aafa.warehouses.util;

import lombok.Getter;

@Getter
public enum RackAllowedByFamilyEnum {
  EST("A", "B", "C"),
  ROB("A", "C", "D");

  private final String[] allowed;

  RackAllowedByFamilyEnum(String... allowed) {
    this.allowed = allowed;
  }


}
