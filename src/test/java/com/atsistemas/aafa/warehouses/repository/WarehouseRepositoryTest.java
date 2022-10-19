package com.atsistemas.aafa.warehouses.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.atsistemas.aafa.warehouses.entity.Warehouse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

@DataJpaTest
class WarehouseRepositoryTest {

  @Autowired
  WarehouseRepository warehouseRepository;

  @Test
  void shouldFindWarehouseById() {
    var warehouse = warehouseRepository.findById(12345);
    assertTrue(warehouse.isPresent());
    assertEquals("Cliente A", warehouse.get().getClient());
    assertEquals("EST", warehouse.get().getFamily());
    assertEquals("0071128c-79c7-43fb-a65d-b26cee852f67", warehouse.get().getUuid());
    assertEquals(4, warehouse.get().getSize());
  }

  @Test
  void shouldFindAllWarehouses() {
    var warehouses = warehouseRepository.findAll();
    assertEquals(3, warehouses.size());
  }

  @Test
  void shouldDeleteWarehouseById() {
    warehouseRepository.deleteById(12345);
    assertThrows(JpaObjectRetrievalFailureException.class,
        () -> warehouseRepository.getReferenceById(12345));
  }

  @Test
  void shouldSaveWarehouse() {
    var warehouseToSave = new Warehouse();
    warehouseToSave.setSize(5);
    warehouseToSave.setClient("Cliente B");
    warehouseToSave.setUuid("b5fc8567-338b-4005-866a-7508b2390a6a");
    warehouseToSave.setFamily("EST");
    var saved = warehouseRepository.save(warehouseToSave);
    assertNotNull(saved.getId());
    assertEquals(5, saved.getSize());
    assertEquals("Cliente B", saved.getClient());
    assertEquals("EST", saved.getFamily());
    assertEquals("b5fc8567-338b-4005-866a-7508b2390a6a", saved.getUuid());
  }

}