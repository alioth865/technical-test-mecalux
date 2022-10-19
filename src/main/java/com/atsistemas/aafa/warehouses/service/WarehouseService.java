package com.atsistemas.aafa.warehouses.service;

import com.atsistemas.aafa.warehouses.entity.Warehouse;
import java.util.List;

public interface WarehouseService {

  Warehouse addWarehouse(Warehouse toWarehouse);

  List<String> allRackPermutationInWarehouse(Long id);

  void deleteWarehouseById(Long id);

  Warehouse getWarehouseById(Long id);

  List<Warehouse> listWarehouses();

  Warehouse updateWarehouseById(Long id, Warehouse toWarehouse);
}
