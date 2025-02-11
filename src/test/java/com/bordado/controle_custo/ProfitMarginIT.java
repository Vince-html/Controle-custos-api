package com.bordado.controle_custo;


import com.bordado.controle_custo.web.dto.PageableDto;

import com.bordado.controle_custo.web.dto.profitMargin.ProfitMarginCreateDTO;
import com.bordado.controle_custo.web.dto.profitMargin.ProfitMarginResponseDTO;
import com.bordado.controle_custo.web.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@Sql(scripts = "/sql/profitMargin/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/profitMargin/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfitMarginIT {

    int id = 11;
    String notFoundById = String.format("Tipo de margem de lucro com ID %s não encontrado.", id);

    @Autowired
    WebTestClient testClient;

    @Test
    public void createProfitMargin_dataValid_returnStatus200() {
        ProfitMarginResponseDTO response = testClient
                .post()
                .uri("/api/v1/profit_margins")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProfitMarginCreateDTO(new BigDecimal(10), "margin 10%"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProfitMarginResponseDTO.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getDescription()).isEqualTo("margin 10%");
        assertThat(response.getProfitValue()).isEqualTo(new BigDecimal(10));
    }

    @Test
    public void createProfitMargin_DataInvalid_returnStatus422() {
         ErrorMessage response = testClient
                .post()
                .uri("/api/v1/profit_margins")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProfitMarginCreateDTO(new BigDecimal(10), ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.getMessage()).isEqualTo("Campo(s) invalido(s)");
    }
    @Test
    public void getProfitMargin_returnStatus200() {
        PageableDto response = testClient
                .get()
                .uri("/api/v1/profit_margins")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getTotalPages()).isEqualTo(1);
        List<Map<String, Object>> expectedList = List.of(
                Map.of(
                        "profitValue", 5.0,
                        "id", 10,
                        "description", "margem 5%"
                ),
                Map.of(
                        "profitValue", 10.0,
                        "id", 20,
                        "description", "margem 10%"
                ),
                Map.of(
                        "profitValue", 15.0,
                        "id", 30,
                        "description", "margem 15%"
                )
        );

        Assertions.assertThat(response.getContent()).isEqualTo(expectedList);
    }

    @Test
    public void editProfitMargin_dataValid_returnStatus200() {
        ProfitMarginResponseDTO response = testClient
                .put()
                .uri("/api/v1/profit_margins/{id}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProfitMarginResponseDTO("Margem 10%", new BigDecimal(10), 10L))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProfitMarginResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getDescription()).isEqualTo("Margem 10%");
    }

    @Test
    public void editProfitMargin_dataValid_returnStatus404() {

        ErrorMessage response = testClient
                .put()
                .uri("/api/v1/profit_margins/{id}", this.id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProfitMarginCreateDTO(new BigDecimal(10), "margin 10%"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
        Assertions.assertThat(response.getMessage()).isEqualTo(this.notFoundById);
    }

    @Test
    public void editProfitMargin_dataValid_returnStatus400() {

        ErrorMessage response = testClient
                .put()
                .uri("/api/v1/profit_margins/{id}", 1011)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("teste")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void deleteProfitMargin_dataValid_returnStatus202() {

        testClient
                .delete()
                .uri("/api/v1/profit_margins/{id}", 10)
                .exchange()
                .expectStatus().isAccepted();

    }

    @Test
    public void deleteProfitMargin_dataValid_returnStatus404() {

        ErrorMessage response = testClient
                .delete()
                .uri("/api/v1/profit_margins/{id}", this.id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
        Assertions.assertThat(response.getMessage()).isEqualTo(this.notFoundById);
    }
}
