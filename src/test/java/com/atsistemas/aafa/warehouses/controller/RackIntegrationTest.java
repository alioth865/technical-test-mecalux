package com.atsistemas.aafa.warehouses.controller;

import static com.atsistemas.aafa.warehouses.factory.DataMother.INVALID_RACK_ID;
import static com.atsistemas.aafa.warehouses.factory.DataMother.INVALID_WAREHOUSE_ID;
import static com.atsistemas.aafa.warehouses.factory.DataMother.VALID_RACK_ID;
import static com.atsistemas.aafa.warehouses.factory.DataMother.VALID_WAREHOUSE_ID;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.atsistemas.aafa.warehouses.factory.JsonReader;
import com.atsistemas.warehouses.model.RackDTORequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class RackIntegrationTest {


  @Autowired
  private WebTestClient webClient;


  @Test
  void shouldGetRackFromWarehouse() {
    webClient.get().uri("/warehouse/{idWarehouse}/rack", VALID_WAREHOUSE_ID)
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void shouldNotFound() {
    webClient.get().uri("/warehouse/{idWarehouse}/rack", INVALID_WAREHOUSE_ID)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void shouldAddRackToWarehouse() {
    webClient.post().uri("/warehouse/{idWarehouse}/rack", VALID_WAREHOUSE_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(JsonReader.stubFromJsonResource("integration/rack/update_rack_A.json",
            RackDTORequest.class))
        .exchange()
        .expectStatus().isOk();
  }


  @Test
  void shouldNotFoundWhenAddRackToWarehouse() {
    webClient.post().uri("/warehouse/{idWarehouse}/rack", INVALID_WAREHOUSE_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(JsonReader.stubFromJsonResource("integration/rack/update_rack_A.json",
            RackDTORequest.class))
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void shouldConflictWhenCannotAddRackToWarehouse() {
    webClient.post().uri("/warehouse/{idWarehouse}/rack", VALID_WAREHOUSE_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(JsonReader.stubFromJsonResource("integration/rack/update_rack_D.json",
            RackDTORequest.class))
        .exchange()
        .expectStatus().is4xxClientError();
  }

  @Test
  void shouldNotFoundIntoDeleteWhenRackIdIsNotFound() {
    webClient.delete()
        .uri("/warehouse/{idWarehouse}/rack/{idRack}", VALID_WAREHOUSE_ID, INVALID_RACK_ID)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void shouldNotFoundIntoDeleteWhenWarehouseIdIsNotFound() {
    webClient.delete()
        .uri("/warehouse/{idWarehouse}/rack/{idRack}", INVALID_WAREHOUSE_ID, VALID_RACK_ID)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void shouldDeleteRackFromWarehouse() {
    webClient.delete()
        .uri("/warehouse/{idWarehouse}/rack/{idRack}", VALID_WAREHOUSE_ID, VALID_RACK_ID)
        .exchange()
        .expectStatus()
        .isNoContent();
  }
}