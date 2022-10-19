package com.atsistemas.aafa.warehouses.factory;

import com.atsistemas.aafa.warehouses.entity.Rack;
import com.atsistemas.aafa.warehouses.entity.Warehouse;
import com.atsistemas.warehouses.model.RackDTO;
import com.atsistemas.warehouses.model.RackDTO.TypeEnum;
import com.atsistemas.warehouses.model.RackDTORequest;
import com.atsistemas.warehouses.model.WarehouseDTO;
import com.atsistemas.warehouses.model.WarehouseDTO.FamilyEnum;
import java.util.ArrayList;
import java.util.List;

public class DataMother {

  public static final int VALID_WAREHOUSE_ID = 12345;

  public static final int VALID_WAREHOUSE_ID_TO_REMOVE = 12346;
  public static final int INVALID_WAREHOUSE_ID = 1234533;
  public static final int VALID_RACK_ID = 1111;
  public static final int INVALID_RACK_ID = 5555;

  public static final String UUID_WAREHOUSE = "3b112b8b-ca07-445b-87ed-166a5f34a07d";
  public static final String CLIENT_A = "CLIENT A";
  public static final String FAMILY_EST = "EST";

  public static final String FAMILY_ROB = "ROB";
  public static final Integer SIZE_WAREHOUSE = 5;

  public static Warehouse createWarehouseEntity(Integer id) {
    return createWarehouseEntity(id, FAMILY_EST);
  }

  public static Warehouse createWarehouseEntity(Integer id, String family, Rack... racks) {
    var warehouse = new Warehouse();
    warehouse.setId(id);
    warehouse.setFamily(family);
    warehouse.setClient(CLIENT_A);
    warehouse.setUuid(UUID_WAREHOUSE);
    warehouse.setSize(SIZE_WAREHOUSE);
    warehouse.setRacks(new ArrayList<>(List.of(racks)));
    return warehouse;
  }


  public static WarehouseDTO createWarehouseModel(Long id) {
    var warehouseDTO = new WarehouseDTO();
    warehouseDTO.setId(id);
    warehouseDTO.setClient(CLIENT_A);
    warehouseDTO.setFamily(FamilyEnum.EST);
    warehouseDTO.setUuid(UUID_WAREHOUSE);
    warehouseDTO.setSize(SIZE_WAREHOUSE);
    return warehouseDTO;
  }

  public static Rack createRackEntity(String type, Integer id) {
    var rack = new Rack();
    rack.setType(type);
    rack.setId(id);
    return rack;
  }

  public static Rack createRackEntity(Integer id) {
    return createRackEntity("A", id);
  }

  public static RackDTO createRackModel(String type, Integer id) {
    var rack = new RackDTO();
    rack.setType(TypeEnum.valueOf(type));
    rack.setId(Long.valueOf(id));
    return rack;
  }

  public static RackDTORequest createRackRequest(String type, Integer id) {
    var rack = new RackDTORequest();
    rack.setId(Long.valueOf(id));
    rack.setType(RackDTORequest.TypeEnum.valueOf(type));
    return rack;
  }


}
