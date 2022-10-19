package com.atsistemas.aafa.warehouses.mapper;

import com.atsistemas.aafa.warehouses.entity.Rack;
import com.atsistemas.warehouses.model.RackDTO;
import com.atsistemas.warehouses.model.RackDTORequest;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RackMapper {

  RackDTO toRackDTO(Rack rack);

  Rack toRack(RackDTO rackDTO);

  List<RackDTO> toRackDTO(List<Rack> racks);

  @Mapping(target = "warehouse", ignore = true)
  Rack toRack(RackDTORequest rack);
}
