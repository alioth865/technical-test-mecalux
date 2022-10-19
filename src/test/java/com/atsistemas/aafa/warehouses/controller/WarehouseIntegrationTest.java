package com.atsistemas.aafa.warehouses.controller;

import static com.atsistemas.aafa.warehouses.factory.DataMother.INVALID_WAREHOUSE_ID;
import static com.atsistemas.aafa.warehouses.factory.DataMother.VALID_WAREHOUSE_ID;
import static com.atsistemas.aafa.warehouses.factory.DataMother.VALID_WAREHOUSE_ID_TO_REMOVE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.atsistemas.aafa.warehouses.factory.JsonReader;
import com.atsistemas.warehouses.model.WarehouseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class WarehouseIntegrationTest {

  @Autowired
  private WebTestClient webClient;

  @Test
  void shouldGetAllWarehouses() {
    webClient.get().uri("/warehouse")
        .exchange()
        .expectStatus()
        .isOk();
  }

  @Test
  void shouldCreateWarehouse() {
    webClient.post().uri("/warehouse")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(
            JsonReader.stubFromJson("integration/warehouse/create_warehouse.json",
                WarehouseDTO.class))
        .exchange()
        .expectStatus().isCreated();
  }

  @Test
  void shouldUpdateWarehouse() {
    webClient.put().uri("/warehouse/{idWarehouse}", VALID_WAREHOUSE_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(
            JsonReader.stubFromJson("integration/warehouse/update_warehouse.json",
                WarehouseDTO.class))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void shouldConflictWhenUpdateWarehouse() {
    webClient.put().uri("/warehouse/{idWarehouse}", VALID_WAREHOUSE_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(
            JsonReader.stubFromJson(
                "integration/warehouse/update_warehouse_unsupported_rack_on_family.json",
                WarehouseDTO.class))
        .exchange()
        .expectStatus().is4xxClientError();
  }

  @Test
  void shouldNotFoundWhenUpdateWarehouse() {
    webClient.put().uri("/warehouse/{idWarehouse}", INVALID_WAREHOUSE_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(
            JsonReader.stubFromJson(
                "integration/warehouse/update_warehouse_unsupported_rack_on_family.json",
                WarehouseDTO.class))
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void shouldNotFoundWhenGetWarehouseById() {
    webClient.get().uri("/warehouse/{idWarehouse}", INVALID_WAREHOUSE_ID)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void shouldGetWarehouseById() {
    webClient.get().uri("/warehouse/{idWarehouse}", VALID_WAREHOUSE_ID)
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void shouldNotFoundWhenRemoveWarehouseById() {
    webClient.delete().uri("/warehouse/{idWarehouse}", INVALID_WAREHOUSE_ID)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void shouldNotContentWhenRemoveWarehouseById() {
    webClient.delete().uri("/warehouse/{idWarehouse}", VALID_WAREHOUSE_ID_TO_REMOVE)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void shouldNotFoundWhenGetAllPossibilities() {
    webClient.get().uri("/warehouse/{idWarehouse}/allPossibilities", INVALID_WAREHOUSE_ID)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void shouldGetAllPossibilities() {
    webClient.get().uri("/warehouse/{idWarehouse}/allPossibilities", VALID_WAREHOUSE_ID)
        .exchange()
        .expectStatus().isOk();
  }

}
