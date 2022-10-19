package com.atsistemas.aafa.warehouses.service.impl;

import static com.atsistemas.aafa.warehouses.factory.DataMother.createRackEntity;
import static com.atsistemas.aafa.warehouses.factory.DataMother.createWarehouseEntity;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.atsistemas.aafa.warehouses.exception.NotFoundException;
import com.atsistemas.aafa.warehouses.exception.UnsupportedRackOnFamily;
import com.atsistemas.aafa.warehouses.repository.RackRepository;
import com.atsistemas.aafa.warehouses.repository.WarehouseRepository;
import com.atsistemas.aafa.warehouses.service.RackService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class RackServiceTest {

  @MockBean
  private RackRepository rackRepository;
  @MockBean
  private WarehouseRepository warehouseRepository;
  @Autowired
  RackService rackService;

  @Test
  void shouldAddTest() {
    var idWarehouse = 111L;
    var idRack = 222L;
    var rack = createRackEntity(Math.toIntExact(idRack));
    var warehouse = createWarehouseEntity(Math.toIntExact(idWarehouse));
    when(warehouseRepository.findById(Math.toIntExact(idWarehouse))).thenReturn(
        Optional.of(warehouse));
    rackService.addRack(idWarehouse, rack);
    verify(rackRepository).save(rack);
  }

  @Test
  void shouldThrowUnsupportedRackOnFamilyException() {
    var idWarehouse = 111L;
    var idRack = 222L;
    var rack = createRackEntity("D", Math.toIntExact(idRack));
    var warehouse = createWarehouseEntity(Math.toIntExact(idWarehouse), "EST");
    when(warehouseRepository.findById(Math.toIntExact(idWarehouse))).thenReturn(
        Optional.of(warehouse));
    Assertions.assertThrows(UnsupportedRackOnFamily.class,
        () -> rackService.addRack(idWarehouse, rack));
  }

  @Test
  void shouldThrowNotFoundWarehouseException() {
    var idWarehouse = 111L;
    var idRack = 222L;
    var rack = createRackEntity("D", Math.toIntExact(idRack));
    when(warehouseRepository.findById(Math.toIntExact(idWarehouse))).thenReturn(
        Optional.empty());
    Assertions.assertThrows(NotFoundException.class,
        () -> rackService.addRack(idWarehouse, rack));
  }

  @Test
  void shouldDeleteRack() {
    var idWarehouse = 111L;
    var idRack = 222L;
    var rack = createRackEntity(Math.toIntExact(idRack));
    var warehouse = createWarehouseEntity(Math.toIntExact(idWarehouse), "EST", rack);

    when(warehouseRepository.findById(Math.toIntExact(idWarehouse))).thenReturn(
        Optional.of(warehouse));
    rackService.deleteRackById(idWarehouse, idRack);
    verify(rackRepository).deleteById(Math.toIntExact(idRack));
  }

  @Test
  void shouldNotFoundExceptionWhenRackIsNotFound() {
    var idWarehouse = 111L;
    var idRack = 222L;
    var warehouse = createWarehouseEntity(Math.toIntExact(idWarehouse));

    when(warehouseRepository.findById(Math.toIntExact(idWarehouse))).thenReturn(
        Optional.of(warehouse));
    Assertions.assertThrows(NotFoundException.class,
        () -> rackService.deleteRackById(idWarehouse, idRack));
  }

  @Test
  void shouldNotFoundExceptionWhenWarehouseIsNotFound() {
    var idWarehouse = 111L;
    var idRack = 222L;
    when(warehouseRepository.findById(Math.toIntExact(idWarehouse))).thenReturn(Optional.empty());
    Assertions.assertThrows(NotFoundException.class,
        () -> rackService.deleteRackById(idWarehouse, idRack));
  }

  @Test
  void shouldListAllRacksInWarehouse() {
    var idWarehouse = 111L;
    var warehouseEntity = createWarehouseEntity(Math.toIntExact(idWarehouse));
    when(warehouseRepository.findById(Math.toIntExact(idWarehouse))).thenReturn(Optional.of(
        warehouseEntity));
    var racks = rackService.listRacks(idWarehouse);
    Assertions.assertEquals(warehouseEntity.getRacks(), racks);
  }

  @Test
  void shouldThorwNotFoundWhenListAllRacksAnWareouseNotExits() {
    var idWarehouse = 111L;
    when(warehouseRepository.findById(Math.toIntExact(idWarehouse))).thenReturn(Optional.empty());
    Assertions.assertThrows(NotFoundException.class, () -> rackService.listRacks(idWarehouse));
  }
}