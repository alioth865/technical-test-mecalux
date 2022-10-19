package com.atsistemas.aafa.warehouses.service.impl;

import static com.atsistemas.aafa.warehouses.factory.DataMother.createRackEntity;
import static com.atsistemas.aafa.warehouses.factory.DataMother.createWarehouseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.atsistemas.aafa.warehouses.entity.Warehouse;
import com.atsistemas.aafa.warehouses.exception.NotFoundException;
import com.atsistemas.aafa.warehouses.exception.UnsupportedRackOnFamily;
import com.atsistemas.aafa.warehouses.repository.WarehouseRepository;
import com.atsistemas.aafa.warehouses.service.WarehouseService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class WarehouseServiceTest {

  @MockBean
  private WarehouseRepository warehouseRepository;

  @Autowired
  private WarehouseService warehouseService;

  @Test
  void shouldAddWarehouse() {
    var idWarehouse = 111;
    var warehouseEntity = createWarehouseEntity(idWarehouse);
    warehouseService.addWarehouse(warehouseEntity);
    Mockito.verify(warehouseRepository).save(warehouseEntity);
  }

  @Test
  void shouldThrowNotFoundWhenIdWarehouseNotFound() {
    var id = 111L;
    when(warehouseRepository.findById(Math.toIntExact(id))).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class,
        () -> warehouseService.allRackPermutationInWarehouse(id));
  }

  @Test
  void shouldCalculateAllPermutation() {
    var idWarehouse = 111L;
    var warehouseEntity = createWarehouseEntity(Math.toIntExact(idWarehouse));
    when(warehouseRepository.findById(Math.toIntExact(idWarehouse))).thenReturn(
        Optional.of(warehouseEntity));

    var allRackPermutationInWarehouse = warehouseService.allRackPermutationInWarehouse(idWarehouse);
    Assertions.assertNotNull(allRackPermutationInWarehouse);
  }

  @Test
  void shouldDeleteWarehouse() {
    var idWarehouse = 111L;
    when(warehouseRepository.existsById(Math.toIntExact(idWarehouse))).thenReturn(true);
    warehouseService.deleteWarehouseById(idWarehouse);
    verify(warehouseRepository).deleteById(Math.toIntExact(idWarehouse));
  }

  @Test
  void shouldNotFoundWhenDeleteWarehouse() {
    var idWarehouse = 111L;
    when(warehouseRepository.existsById(Math.toIntExact(idWarehouse))).thenReturn(false);
    assertThrows(NotFoundException.class,
        () -> warehouseService.deleteWarehouseById(idWarehouse));
  }


  @Test
  void shouldGetWarehouseById() {
    var idWarehouse = 111L;
    var warehouseEntity = Optional.of(createWarehouseEntity(Math.toIntExact(idWarehouse)));
    when(warehouseRepository.findById(Math.toIntExact(idWarehouse))).thenReturn(warehouseEntity);
    var actual = warehouseService.getWarehouseById(idWarehouse);
    Assertions.assertEquals(warehouseEntity.get(), actual);
  }

  @Test
  void shouldNotFoundWhenGetWarehouseByNotFoundId() {
    var idWarehouse = 111L;
    when(warehouseRepository.findById(Math.toIntExact(idWarehouse))).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> warehouseService.getWarehouseById(idWarehouse));
  }

  @Test
  void shouldReturnAllWarehouses() {
    var warehouseEntity = createWarehouseEntity(111);
    when(warehouseRepository.findAll()).thenReturn(List.of(warehouseEntity));
    var actual = warehouseService.listWarehouses();
    Assertions.assertEquals(1, actual.size());
  }

  @Test
  void shouldUpdateWarehouse() {
    var warehouseId = 111L;
    // Current
    var rackTypeA = createRackEntity("A", 222);
    var currentWarehouse = createWarehouseEntity(Math.toIntExact(warehouseId), "EST", rackTypeA);
    // To Update
    var rackTypeB = createRackEntity("B", 333);
    var newWarehouse = createWarehouseEntity(Math.toIntExact(warehouseId), "EST", rackTypeA,
        rackTypeB);

    when(warehouseRepository.existsById(any())).thenReturn(true);
    when(warehouseRepository.getReferenceById(any())).thenReturn(currentWarehouse);
    when(warehouseRepository.save(newWarehouse)).thenReturn(newWarehouse);

    var actual = warehouseService.updateWarehouseById(warehouseId, newWarehouse);
    assertEquals(newWarehouse, actual);
   /* warehouseRepository.existsById
    warehouseRepository.getReferenceById
    warehouseRepository.save*/
  }

  @Test
  void shouldUnsupportedRackOnFamily() {
    var warehouseId = 111L;
    // Current
    var rackTypeA = createRackEntity("A", 222);
    var rackTypeB = createRackEntity("B", 333);
    var currentWarehouse = createWarehouseEntity(Math.toIntExact(warehouseId), "EST", rackTypeA,
        rackTypeB);

    // To Update
    var newWarehouse = createWarehouseEntity(Math.toIntExact(warehouseId), "ROB");

    when(warehouseRepository.existsById(any())).thenReturn(true);
    when(warehouseRepository.getReferenceById(any())).thenReturn(currentWarehouse);
    when(warehouseRepository.save(newWarehouse)).thenReturn(newWarehouse);

    assertThrows(UnsupportedRackOnFamily.class,
        () -> warehouseService.updateWarehouseById(warehouseId, newWarehouse));

  }

  @Test
  void shouldNotFoundExceptionWhenUpdate() {
    when(warehouseRepository.existsById(any())).thenReturn(false);
    assertThrows(NotFoundException.class,
        () -> warehouseService.updateWarehouseById(1L, new Warehouse()));
  }


}