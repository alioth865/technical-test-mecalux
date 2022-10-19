package com.atsistemas.aafa.warehouses.service.impl;

import static com.atsistemas.aafa.warehouses.util.PermutationUtil.calculateAllPermutations;
import static com.atsistemas.aafa.warehouses.util.PermutationUtil.canBeInstalled;

import com.atsistemas.aafa.warehouses.entity.Rack;
import com.atsistemas.aafa.warehouses.entity.Warehouse;
import com.atsistemas.aafa.warehouses.exception.ErrorEnum;
import com.atsistemas.aafa.warehouses.exception.NotFoundException;
import com.atsistemas.aafa.warehouses.exception.UnsupportedRackOnFamily;
import com.atsistemas.aafa.warehouses.repository.WarehouseRepository;
import com.atsistemas.aafa.warehouses.service.WarehouseService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl implements WarehouseService {

  private final WarehouseRepository warehouseRepository;

  public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
    this.warehouseRepository = warehouseRepository;
  }

  @Override
  public Warehouse addWarehouse(Warehouse toWarehouse) {
    return warehouseRepository.save(toWarehouse);
  }

  @Override
  public List<String> allRackPermutationInWarehouse(Long id) {
    var warehouse = warehouseRepository.findById(Math.toIntExact(id));
    if (warehouse.isEmpty()) {
      throw new NotFoundException(ErrorEnum.NOT_FOUND_WAREHOUSE.getCode(),
          String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), id));
    } else {
      return calculateAllPermutations(warehouse.get().getFamily(), warehouse.get().getSize());
    }
  }


  @Override
  public void deleteWarehouseById(Long id) {
    if (warehouseRepository.existsById(Math.toIntExact(id))) {
      warehouseRepository.deleteById(Math.toIntExact(id));
    } else {
      throw new NotFoundException(ErrorEnum.NOT_FOUND_WAREHOUSE.getCode(),
          String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), id));
    }
  }

  @Override
  public Warehouse getWarehouseById(Long id) {
    var warehouseOptional = warehouseRepository.findById(Math.toIntExact(id));
    if (warehouseOptional.isEmpty()) {
      throw new NotFoundException(ErrorEnum.NOT_FOUND_WAREHOUSE.getCode(),
          String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), id));
    } else {
      return warehouseOptional.get();
    }
  }

  @Override
  public List<Warehouse> listWarehouses() {
    return warehouseRepository.findAll();
  }

  @Override
  public Warehouse updateWarehouseById(Long idWarehouse, Warehouse warehouse) {
    if (warehouseRepository.existsById(Math.toIntExact(idWarehouse))) {
      var current = warehouseRepository.getReferenceById(Math.toIntExact(idWarehouse));
      var canBeInstalled = true;
      if (current.getRacks() != null) {
        canBeInstalled = current.getRacks().stream()
            .allMatch(rack -> canBeInstalled(rack, warehouse));
      }
      if (canBeInstalled) {
        current.setClient(warehouse.getClient());
        current.setFamily(warehouse.getFamily());
        current.setUuid(warehouse.getUuid());
        current.setSize(warehouse.getSize());
        return warehouseRepository.save(current);
      } else {
        var racks = current.getRacks().stream()
            .map(Rack::getType)
            .collect(Collectors.toList());
        throw new UnsupportedRackOnFamily(ErrorEnum.UNSUPPORTED_RACK_ON_FAMILY.getCode(),
            String.format(ErrorEnum.UNSUPPORTED_RACK_ON_FAMILY.getDetail(), racks,
                warehouse.getFamily()));
      }
    } else {
      throw new NotFoundException(ErrorEnum.NOT_FOUND_WAREHOUSE.getCode(),
          String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), idWarehouse));
    }
  }
}
