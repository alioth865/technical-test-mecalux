package com.atsistemas.aafa.warehouses.controller;

import com.atsistemas.aafa.warehouses.mapper.RackMapper;
import com.atsistemas.aafa.warehouses.service.RackService;
import com.atsistemas.warehouses.api.RackApi;
import com.atsistemas.warehouses.model.RackDTO;
import com.atsistemas.warehouses.model.RackDTORequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RackRestController implements RackApi {

  private final RackService rackService;
  private final RackMapper rackMapper;

  public RackRestController(RackService rackService, RackMapper rackMapper) {
    this.rackService = rackService;
    this.rackMapper = rackMapper;
  }


  @Override
  public ResponseEntity<RackDTO> addRack(Long idWarehouse, RackDTORequest rackDTORequest) {
    var rack = rackService.addRack(idWarehouse, rackMapper.toRack(rackDTORequest));
    return ResponseEntity.ok(rackMapper.toRackDTO(rack));
  }

  @Override
  public ResponseEntity<Void> deleteRackById(Long idWarehouse, Long idRack) {
    rackService.deleteRackById(idWarehouse, idRack);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<List<RackDTO>> listRacks(Long idWarehouse) {
    var racks = rackService.listRacks(idWarehouse);
    return ResponseEntity.ok(rackMapper.toRackDTO(racks));
  }
}
