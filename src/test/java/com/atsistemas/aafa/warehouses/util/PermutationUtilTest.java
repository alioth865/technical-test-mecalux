package com.atsistemas.aafa.warehouses.util;

import static com.atsistemas.aafa.warehouses.factory.DataMother.createRackEntity;
import static com.atsistemas.aafa.warehouses.factory.DataMother.createWarehouseEntity;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.atsistemas.aafa.warehouses.exception.UknownFamily;
import com.atsistemas.warehouses.model.WarehouseDTO.FamilyEnum;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PermutationUtilTest {

  @Test
  void shouldCalculateAllPermutations() {
    var actual = PermutationUtil.calculateAllPermutations(FamilyEnum.EST.toString(), 3);
    var expected = List.of(
        "AAA", "AAB", "AAC", "ABA", "ABB", "ABC", "ACA", "ACB", "ACC",
        "BAA", "BAB", "BAC", "BBA", "BBB", "BBC", "BCA", "BCB", "BCC",
        "CAA", "CAB", "CAC", "CBA", "CBB", "CBC", "CCA", "CCB", "CCC");
    Assertions.assertThat(actual).hasSameElementsAs(expected);
  }

  @Test
  void shouldCanBeInstalled() {
    var rack = createRackEntity("A", 111);
    var warehouse = createWarehouseEntity(222, "EST");
    var canBeInstalled = PermutationUtil.canBeInstalled(rack, warehouse);
    assertTrue(canBeInstalled);
  }

  @Test
  void shouldCanNotBeInstalled() {
    var rack = createRackEntity("D", 111);
    var warehouse = createWarehouseEntity(222, "EST");
    var canBeInstalled = PermutationUtil.canBeInstalled(rack, warehouse);
    assertFalse(canBeInstalled);
  }

  @Test
  void shouldThrowUknownFamilyException() {
    assertThrows(UknownFamily.class, () -> PermutationUtil.calculateAllPermutations("UNKNOWN", 4));
  }
}