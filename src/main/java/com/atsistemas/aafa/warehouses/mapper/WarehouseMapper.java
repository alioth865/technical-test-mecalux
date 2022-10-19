package com.atsistemas.aafa.warehouses.mapper;

import com.atsistemas.aafa.warehouses.entity.Warehouse;
import com.atsistemas.warehouses.model.WarehouseDTO;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

  WarehouseDTO toWarehouseDto(Warehouse warehouse);

  List<WarehouseDTO> toWarehouseDto(List<Warehouse> warehouse);

  Warehouse toWarehouse(WarehouseDTO warehouseDTO);

}
