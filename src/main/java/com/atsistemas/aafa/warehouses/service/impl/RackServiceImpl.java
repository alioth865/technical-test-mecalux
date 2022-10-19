package com.atsistemas.aafa.warehouses.service.impl;

import static com.atsistemas.aafa.warehouses.util.PermutationUtil.canBeInstalled;

import com.atsistemas.aafa.warehouses.entity.Rack;
import com.atsistemas.aafa.warehouses.exception.ErrorEnum;
import com.atsistemas.aafa.warehouses.exception.NotFoundException;
import com.atsistemas.aafa.warehouses.exception.UnsupportedRackOnFamily;
import com.atsistemas.aafa.warehouses.repository.RackRepository;
import com.atsistemas.aafa.warehouses.repository.WarehouseRepository;
import com.atsistemas.aafa.warehouses.service.RackService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RackServiceImpl implements RackService {

  private final RackRepository rackRepository;
  private final WarehouseRepository warehouseRepository;

  public RackServiceImpl(RackRepository rackRepository, WarehouseRepository warehouseRepository) {
    this.rackRepository = rackRepository;
    this.warehouseRepository = warehouseRepository;
  }

  @Override
  public Rack addRack(Long idWarehouse, Rack rack) {
    var warehouseOptional = warehouseRepository.findById(Math.toIntExact(idWarehouse));
    if (warehouseOptional.isPresent()) {
      var warehouse = warehouseOptional.get();
      if (canBeInstalled(rack, warehouse)) {
        rack.setWarehouse(warehouse);
        return rackRepository.save(rack);
      } else {
        throw new UnsupportedRackOnFamily(ErrorEnum.UNSUPPORTED_RACK_ON_FAMILY.getCode(),
            String.format(ErrorEnum.UNSUPPORTED_RACK_ON_FAMILY.getDetail(), rack.getType(),
                warehouse.getFamily()));
      }
    } else {
      throw new NotFoundException(ErrorEnum.NOT_FOUND_WAREHOUSE.getCode(),
          String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), idWarehouse));
    }
  }

  @Override
  public void deleteRackById(Long idWarehouse, Long idRack) {
    // To check warehouse exists
    var warehouseOptional = warehouseRepository.findById(Math.toIntExact(idWarehouse));
    if (warehouseOptional.isPresent()) {
      var warehouse = warehouseOptional.get();
      var isPresent = warehouse.getRacks().stream()
          .anyMatch(rack -> rack.getId().equals(Math.toIntExact(idRack)));
      if (isPresent) {
        rackRepository.deleteById(Math.toIntExact(idRack));
      } else {
        throw new NotFoundException(ErrorEnum.NOT_FOUND_RACK.getCode(),
            String.format(ErrorEnum.NOT_FOUND_RACK.getDetail(), idRack));
      }
    } else {
      throw new NotFoundException(ErrorEnum.NOT_FOUND_WAREHOUSE.getCode(),
          String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), idWarehouse));
    }
  }

  @Override
  public List<Rack> listRacks(Long idWarehouse) {
    var warehouseOptional = warehouseRepository.findById(Math.toIntExact(idWarehouse));
    if (warehouseOptional.isPresent()) {
      return warehouseOptional.get().getRacks();
    } else {
      throw new NotFoundException(ErrorEnum.NOT_FOUND_WAREHOUSE.getCode(),
          String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), idWarehouse));
    }


  }

}
