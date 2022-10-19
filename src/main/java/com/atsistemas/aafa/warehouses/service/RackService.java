package com.atsistemas.aafa.warehouses.service;

import com.atsistemas.aafa.warehouses.entity.Rack;
import java.util.List;

public interface RackService {

  Rack addRack(Long idWarehouse, Rack rack);

  void deleteRackById(Long idWarehouse, Long idRack);

  List<Rack> listRacks(Long idWarehouse);
}
