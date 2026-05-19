package com.mbathegamer.budgetto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.mbathegamer.budgetto.controllers.HealthController;
import com.mbathegamer.budgetto.dtos.HealthResponse;

@SpringBootTest
public class HealthTest {
  private final RestTestClient client =
      RestTestClient.bindToController(new HealthController()).build();

  @Test
  void shouldReturnAlive() {
    client.get().uri("/health")
        .exchange()
        .expectStatus().isOk()
        .expectBody(HealthResponse.class).isEqualTo(new HealthResponse("alive"));
  }
}
