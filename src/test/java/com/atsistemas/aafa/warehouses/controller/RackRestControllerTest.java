package com.atsistemas.aafa.warehouses.controller;

import static com.atsistemas.aafa.warehouses.factory.DataMother.createRackEntity;
import static com.atsistemas.aafa.warehouses.factory.DataMother.createRackModel;
import static com.atsistemas.aafa.warehouses.factory.DataMother.createRackRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.atsistemas.aafa.warehouses.exception.ErrorEnum;
import com.atsistemas.aafa.warehouses.exception.NotFoundException;
import com.atsistemas.aafa.warehouses.exception.UnsupportedRackOnFamily;
import com.atsistemas.aafa.warehouses.mapper.RackMapper;
import com.atsistemas.aafa.warehouses.service.RackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RackRestController.class)
class RackRestControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private RackService rackService;

  @MockBean
  private RackMapper rackMapper;

  private ObjectMapper objectMapper;


  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
  }

  @Test
  @SneakyThrows
  void shouldReturnAllRacksWhenWarehouseIsFound() {
    Integer warehouseId = 1;
    var racksModel = List.of(createRackModel("A", 1));
    var racksEntity = List.of(createRackEntity("A", 1));
    when(rackService.listRacks(any())).thenReturn(racksEntity);
    when(rackMapper.toRackDTO(racksEntity)).thenReturn(racksModel);
    mvc.perform(get("/warehouse/{idWarehouse}/rack", warehouseId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1));
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundWhenWarehouseIsNotFound() {
    var id = 1L;
    var detail = String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), id);
    var code = ErrorEnum.NOT_FOUND_WAREHOUSE.getCode();
    doThrow(new NotFoundException(code, detail)).when(rackService).listRacks(id);

    mvc.perform(get("/warehouse/{idWarehouse}/rack", id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(code))
        .andExpect(jsonPath("$.detail").value(detail));
  }

  @Test
  @SneakyThrows
  void shouldAddRackWhenWarehouseIsFoundAndRackIsAllowed() {
    long id = 1L;
    var rackEntity = createRackEntity("A", Math.toIntExact(id));
    var rackDtoRequest = createRackRequest("A", Math.toIntExact(id));
    var rackModel = createRackModel("A", Math.toIntExact(id));

    when(rackService.addRack(id, rackEntity)).thenReturn(rackEntity);
    when(rackMapper.toRack(rackDtoRequest)).thenReturn(rackEntity);
    when(rackMapper.toRackDTO(rackEntity)).thenReturn(rackModel);

    mvc.perform(
            post("/warehouse/{idWarehouse}/rack", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rackEntity))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.type").value("A"))
        .andExpect(jsonPath("$.id").value(id));
  }

  @Test
  @SneakyThrows
  void shouldNotFoundWhenAddRackButWarehouseIsNotFound() {
    long id = 1L;
    var rackEntity = createRackEntity("A", Math.toIntExact(id));
    var rackDtoRequest = createRackRequest("A", Math.toIntExact(id));

    var detail = String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), id);
    var code = ErrorEnum.NOT_FOUND_WAREHOUSE.getCode();
    doThrow(new NotFoundException(code, detail)).when(rackService).addRack(id, rackEntity);
    when(rackMapper.toRack(rackDtoRequest)).thenReturn(rackEntity);

    mvc.perform(
            post("/warehouse/{idWarehouse}/rack", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rackEntity))
        )
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(code))
        .andExpect(jsonPath("$.detail").value(detail));
  }

  @Test
  @SneakyThrows
  void shouldConflictWhenAddRackButIsNotAllowedInWarehouse() {
    long id = 1L;
    var rackEntity = createRackEntity("A", Math.toIntExact(id));
    var rackDtoRequest = createRackRequest("A", Math.toIntExact(id));

    String type = "D";
    String family = "EST";
    var detail = String.format(ErrorEnum.UNSUPPORTED_RACK_ON_FAMILY.getDetail(), type, family);
    var code = ErrorEnum.UNSUPPORTED_RACK_ON_FAMILY.getCode();
    doThrow(new UnsupportedRackOnFamily(code, detail)).when(rackService).addRack(id, rackEntity);
    when(rackMapper.toRack(rackDtoRequest)).thenReturn(rackEntity);

    mvc.perform(
            post("/warehouse/{idWarehouse}/rack", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rackEntity)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value(code))
        .andExpect(jsonPath("$.detail").value(detail));
  }

  @Test
  @SneakyThrows
  void shouldNotFoundWhenDeleteRackButWarehouseIsNotFound() {
    var idWarehouse = 111L;
    var idRack = 222L;
    String code = ErrorEnum.NOT_FOUND_WAREHOUSE.getCode();
    String detail = String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), idWarehouse);
    doThrow(new NotFoundException(code, detail)).when(rackService)
        .deleteRackById(idWarehouse, idRack);
    mvc.perform(delete("/warehouse/{idWarehouse}/rack/{idRack}", idWarehouse, idRack))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(code))
        .andExpect(jsonPath("$.detail").value(detail));
  }

  @Test
  @SneakyThrows
  void shouldNotFoundWhenDeleteRackButRackIsNotFound() {
    var idWarehouse = 111L;
    var idRack = 222L;
    String code = ErrorEnum.NOT_FOUND_RACK.getCode();
    String detail = String.format(ErrorEnum.NOT_FOUND_RACK.getDetail(), idRack);
    doThrow(new NotFoundException(code, detail)).when(rackService)
        .deleteRackById(idWarehouse, idRack);

    mvc.perform(delete("/warehouse/{idWarehouse}/rack/{idRack}", idWarehouse, idRack))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(code))
        .andExpect(jsonPath("$.detail").value(detail));
  }

  @Test
  @SneakyThrows
  void shouldNotContentWhenDeleteRack() {
    var idWarehouse = 111L;
    var idRack = 222L;
    doNothing().when(rackService).deleteRackById(idWarehouse, idRack);
    mvc.perform(delete("/warehouse/{idWarehouse}/rack/{idRack}", idWarehouse, idRack))
        .andExpect(status().isNoContent());
  }


}
