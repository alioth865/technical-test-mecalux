package com.atsistemas.aafa.warehouses.controller;

import com.atsistemas.aafa.warehouses.mapper.WarehouseMapper;
import com.atsistemas.aafa.warehouses.service.WarehouseService;
import com.atsistemas.warehouses.api.WarehouseApi;
import com.atsistemas.warehouses.model.WarehouseDTO;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WarehouseRestController implements WarehouseApi {

  private final WarehouseService warehouseService;
  private final WarehouseMapper warehouseMapper;

  public WarehouseRestController(WarehouseService warehouseService,
      WarehouseMapper warehouseMapper) {
    this.warehouseService = warehouseService;
    this.warehouseMapper = warehouseMapper;
  }

  @Override
  public ResponseEntity<WarehouseDTO> addWarehouse(WarehouseDTO warehouse) {
    var toAdd = warehouseMapper.toWarehouse((warehouse));
    var added = warehouseService.addWarehouse(toAdd);
    return new ResponseEntity<>(warehouseMapper.toWarehouseDto(added),
        HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<List<String>> allRackPermutationInWarehouse(Long id) {
    return ResponseEntity.ok(warehouseService.allRackPermutationInWarehouse(id));
  }

  @Override
  public ResponseEntity<Void> deleteWarehouseById(Long id) {
    warehouseService.deleteWarehouseById(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<WarehouseDTO> getWarehouseById(Long id) {
    var warehouseById = warehouseService.getWarehouseById(id);
    return ResponseEntity.ok(warehouseMapper.toWarehouseDto(warehouseById));
  }

  @Override
  public ResponseEntity<List<WarehouseDTO>> listWarehouses() {
    var warehouses = warehouseService.listWarehouses();
    return ResponseEntity.ok(warehouseMapper.toWarehouseDto(warehouses));
  }

  @Override
  public ResponseEntity<WarehouseDTO> updateWarehouseById(Long id, WarehouseDTO warehouse) {
    var retVal = warehouseService.updateWarehouseById(id,
        warehouseMapper.toWarehouse(warehouse));
    return ResponseEntity.ok(warehouseMapper.toWarehouseDto(retVal));
  }
}
