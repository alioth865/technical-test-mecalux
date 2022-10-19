package com.atsistemas.aafa.warehouses.controller;

import static com.atsistemas.aafa.warehouses.factory.DataMother.CLIENT_A;
import static com.atsistemas.aafa.warehouses.factory.DataMother.FAMILY_EST;
import static com.atsistemas.aafa.warehouses.factory.DataMother.SIZE_WAREHOUSE;
import static com.atsistemas.aafa.warehouses.factory.DataMother.UUID_WAREHOUSE;
import static com.atsistemas.aafa.warehouses.factory.DataMother.createWarehouseEntity;
import static com.atsistemas.aafa.warehouses.factory.DataMother.createWarehouseModel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.atsistemas.aafa.warehouses.entity.Warehouse;
import com.atsistemas.aafa.warehouses.exception.ErrorEnum;
import com.atsistemas.aafa.warehouses.exception.NotFoundException;
import com.atsistemas.aafa.warehouses.exception.UnsupportedRackOnFamily;
import com.atsistemas.aafa.warehouses.mapper.WarehouseMapper;
import com.atsistemas.aafa.warehouses.service.WarehouseService;
import com.atsistemas.aafa.warehouses.util.RackAllowedByFamilyEnum;
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

@WebMvcTest(WarehouseRestController.class)
class WarehouseRestControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private WarehouseService warehouseService;

  @MockBean
  private WarehouseMapper warehouseMapper;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
  }

  @Test
  @SneakyThrows
  void shouldAddWarehouse() {
    var id = 1L;
    var warehouseModelWithIdNull = createWarehouseModel(null);
    var warehouseEntityWithIdNull = createWarehouseEntity(null);
    var warehouseModelWithId = createWarehouseModel(id);
    var warehouseEntityWithId = createWarehouseEntity(Math.toIntExact(id));

    when(warehouseMapper.toWarehouse(warehouseModelWithIdNull)).thenReturn(
        warehouseEntityWithIdNull);
    when(warehouseMapper.toWarehouseDto(warehouseEntityWithId)).thenReturn(warehouseModelWithId);
    when(warehouseService.addWarehouse(warehouseEntityWithIdNull)).thenReturn(
        warehouseEntityWithId);

    mvc.perform(
            post("/warehouse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseModelWithIdNull))
        )
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.client").value(CLIENT_A))
        .andExpect(jsonPath("$.family").value(FAMILY_EST))
        .andExpect(jsonPath("$.uuid").value(UUID_WAREHOUSE))
        .andExpect(jsonPath("$.size").value(SIZE_WAREHOUSE));
  }

  @Test
  @SneakyThrows
  void shouldReturnAllPermutation() {
    var id = 1L;
    when(warehouseService.allRackPermutationInWarehouse(id)).thenReturn(List.of("A", "B", "C"));

    mvc.perform(
            get("/warehouse/{idWarehouse}/allPossibilities", String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.size()").value(3))
        .andExpect(jsonPath("$[0]").value("A"))
        .andExpect(jsonPath("$[1]").value("B"))
        .andExpect(jsonPath("$[2]").value("C"));
  }

  @Test
  @SneakyThrows
  void shouldReturnNotContentWhenWarehouseIsDeleted() {
    var id = 1L;
    doNothing().when(warehouseService).deleteWarehouseById(id);

    mvc.perform(delete("/warehouse/{idWarehouse}", id))
        .andExpect(status().isNoContent());
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundWhenWarehouseIdIsNotFound() {
    var id = 1L;
    var detail = String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), id);
    var code = ErrorEnum.NOT_FOUND_WAREHOUSE.getCode();
    doThrow(new NotFoundException(code, detail)).when(warehouseService).deleteWarehouseById(id);

    mvc.perform(delete("/warehouse/{idWarehouse}", id))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(code))
        .andExpect(jsonPath("$.detail").value(detail));
  }

  @Test
  @SneakyThrows
  void shouldReturnWarehouseById() {
    var id = 1L;
    var warehouseEntityWithId = createWarehouseEntity(Math.toIntExact(id));
    var warehouseModelWithId = createWarehouseModel(id);

    when(warehouseService.getWarehouseById(id)).thenReturn(warehouseEntityWithId);
    when(warehouseMapper.toWarehouseDto(warehouseEntityWithId)).thenReturn(warehouseModelWithId);
    mvc.perform(get("/warehouse/{idWarehouse}", id))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.client").value(CLIENT_A))
        .andExpect(jsonPath("$.family").value(FAMILY_EST))
        .andExpect(jsonPath("$.uuid").value(UUID_WAREHOUSE))
        .andExpect(jsonPath("$.size").value(SIZE_WAREHOUSE));
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundWhenFindByIdIsNotFound() {
    var id = 1L;
    var detail = String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), id);
    var code = ErrorEnum.NOT_FOUND_WAREHOUSE.getCode();
    doThrow(new NotFoundException(code, detail)).when(warehouseService).getWarehouseById(id);

    mvc.perform(get("/warehouse/{idWarehouse}", id))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(code))
        .andExpect(jsonPath("$.detail").value(detail));
  }

  @Test
  @SneakyThrows
  void shouldReturnAllWarehouses() {
    var id = 1L;
    var warehouseEntityWithId = createWarehouseEntity(Math.toIntExact(id));
    var warehouseModelWithId = createWarehouseModel(id);
    var warehouseEntityList = List.of(warehouseEntityWithId);
    var warehouseModelList = List.of(warehouseModelWithId);
    when(warehouseService.listWarehouses()).thenReturn(warehouseEntityList);
    when(warehouseMapper.toWarehouseDto(warehouseEntityList)).thenReturn(warehouseModelList);
    mvc.perform(get("/warehouse"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1));
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestWhenRackIsNotAllowed() {
    var detail = String.format(ErrorEnum.UNSUPPORTED_RACK_ON_FAMILY.getDetail(), List.of("D"),
        RackAllowedByFamilyEnum.EST);
    var code = ErrorEnum.UNSUPPORTED_RACK_ON_FAMILY.getCode();

    doThrow(new UnsupportedRackOnFamily(code, detail)).when(warehouseService)
        .updateWarehouseById(any(), any());

    mvc.perform(
            put("/warehouse/{idWarehouse}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Warehouse()))
        )
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(code))
        .andExpect(jsonPath("$.detail").value(detail));
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundWhenUpdateWarehouseIdIsNotFound() {
    var id = 1L;
    var detail = String.format(ErrorEnum.NOT_FOUND_WAREHOUSE.getDetail(), id);
    var code = ErrorEnum.NOT_FOUND_WAREHOUSE.getCode();
    doThrow(new NotFoundException(code, detail)).when(warehouseService)
        .updateWarehouseById(any(), any());

    mvc.perform(
            put("/warehouse/{idWarehouse}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Warehouse()))
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(code))
        .andExpect(jsonPath("$.detail").value(detail));
  }

  @Test
  @SneakyThrows
  void shouldReturnOkWhenWarehouseIsUpdated() {
    var id = 1L;
    var warehouseEntityWithId = createWarehouseEntity(Math.toIntExact(id));
    var warehouseModelWithId = createWarehouseModel(id);
    when(warehouseService.updateWarehouseById(id, warehouseEntityWithId)).thenReturn(
        warehouseEntityWithId);
    when(warehouseMapper.toWarehouse(warehouseModelWithId)).thenReturn(warehouseEntityWithId);
    when(warehouseMapper.toWarehouseDto(warehouseEntityWithId)).thenReturn(warehouseModelWithId);

    mvc.perform(
            put("/warehouse/{idWarehouse}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseModelWithId))
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.client").value(CLIENT_A))
        .andExpect(jsonPath("$.family").value(FAMILY_EST))
        .andExpect(jsonPath("$.uuid").value(UUID_WAREHOUSE))
        .andExpect(jsonPath("$.size").value(SIZE_WAREHOUSE));

  }


}