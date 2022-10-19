package com.atsistemas.aafa.warehouses.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.atsistemas.aafa.warehouses.entity.Rack;
import com.atsistemas.aafa.warehouses.entity.Warehouse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

@DataJpaTest
class RackRepositoryTest {

  @Autowired
  RackRepository rackRepository;

  @Test
  void shouldFindRackById() {
    var rack = rackRepository.findById(1111);
    assertTrue(rack.isPresent());
    assertEquals("A", rack.get().getType());
    assertEquals("5d2f9f2a-d546-4b4c-9452-b36fa6b4f1a5", rack.get().getUuid());
  }

  @Test
  void shouldFindAllRacks() {
    var racks = rackRepository.findAll();
    assertEquals(4, racks.size());
  }

  @Test
  void shouldDeleteRackById() {
    rackRepository.deleteById(1111);
    assertThrows(JpaObjectRetrievalFailureException.class,
        () -> rackRepository.getReferenceById(1111));
  }

  @Test
  void shouldSaveRack() {
    var warehouseToSave = new Warehouse();
    warehouseToSave.setId(12345);
    var rackToSave = new Rack();
    rackToSave.setType("A");
    rackToSave.setUuid("23692a7c-9109-4b25-877f-ef0225c8c439");
    rackToSave.setWarehouse(warehouseToSave);
    var saved = rackRepository.save(rackToSave);
    assertNotNull(saved.getId());
    assertEquals("A", saved.getType());
    assertEquals("23692a7c-9109-4b25-877f-ef0225c8c439", saved.getUuid());
    assertEquals(12345, saved.getWarehouse().getId());
  }
}