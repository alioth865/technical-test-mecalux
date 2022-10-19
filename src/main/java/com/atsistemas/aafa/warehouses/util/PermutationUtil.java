package com.atsistemas.aafa.warehouses.util;

import com.atsistemas.aafa.warehouses.entity.Rack;
import com.atsistemas.aafa.warehouses.entity.Warehouse;
import com.atsistemas.aafa.warehouses.exception.ErrorEnum;
import com.atsistemas.aafa.warehouses.exception.UknownFamily;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PermutationUtil {

  private PermutationUtil() {
  }

  public static List<String> calculateAllPermutations(String family, Integer size) {
    switch (family) {
      case "EST":
        return calculateAllPermutations(size, RackAllowedByFamilyEnum.EST, "");
      case "ROB":
        return calculateAllPermutations(size, RackAllowedByFamilyEnum.ROB, "");
      default:
        throw new UknownFamily(ErrorEnum.UNKNOWN_FAMILY.getCode(),
            String.format(ErrorEnum.UNKNOWN_FAMILY.getDetail(), family));
    }
  }

  private static List<String> calculateAllPermutations(Integer size,
      RackAllowedByFamilyEnum rackAllowedByFamilyEnum,
      String current) {
    var retVal = new ArrayList<String>();
    if (size == 1) {
      for (String allow : rackAllowedByFamilyEnum.getAllowed()) {
        retVal.add(current.concat(allow));
      }
    } else {
      for (String allow : rackAllowedByFamilyEnum.getAllowed()) {
        retVal.addAll(
            calculateAllPermutations(size - 1, rackAllowedByFamilyEnum, current.concat(allow)));
      }
    }
    return retVal;
  }

  public static boolean canBeInstalled(Rack rack, Warehouse warehouse) {
    var rackAllowedByFamilyEnum = RackAllowedByFamilyEnum.valueOf(warehouse.getFamily());
    return Arrays.stream(rackAllowedByFamilyEnum.getAllowed())
        .anyMatch(allowed -> allowed.contains(rack.getType()));
  }

}
