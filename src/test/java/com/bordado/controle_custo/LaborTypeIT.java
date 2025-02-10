package com.bordado.controle_custo;


import com.bordado.controle_custo.web.dto.PageableDto;
import com.bordado.controle_custo.web.dto.labortypes.LaborTypesCreateDTO;
import com.bordado.controle_custo.web.dto.labortypes.LaborTypesResponseDTO;
import com.bordado.controle_custo.web.dto.supplier.SupplierCreateDTO;
import com.bordado.controle_custo.web.dto.supplier.SupplierResponseDTO;
import com.bordado.controle_custo.web.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Sql(scripts = "/sql/laborType/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/laborType/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LaborTypeIT {

    int id = 11;
    String notFoundById = String.format("Tipo de mão de obra com ID %s não encontrado.", id);

    @Autowired
    WebTestClient testClient;

    @Test
    public void createRawMaterial_dataValid_returnStatus200() {
        LaborTypesResponseDTO response = testClient
                .post()
                .uri("/api/v1/labor_types")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LaborTypesCreateDTO("pintura", new BigDecimal(10)))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(LaborTypesResponseDTO.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getTypeLabor()).isEqualTo("pintura");
        assertThat(response.getCost()).isEqualTo(new BigDecimal(10));
    }

    @Test
    public void createRawMaterial_Conflict_returnStatus409() {
        ErrorMessage response = testClient
                .post()
                .uri("/api/v1/labor_types")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LaborTypesCreateDTO("bordado", new BigDecimal(10)))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(409);
        assertThat(response.getMessage()).isEqualTo("Tipo de mão de obra já cadastrado.");
    }

    @Test
    public void createRawMaterial_DataInvalid_returnStatus422() {
         ErrorMessage response = testClient
                .post()
                .uri("/api/v1/labor_types")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LaborTypesCreateDTO("", new BigDecimal(10)))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.getMessage()).isEqualTo("Campo(s) invalido(s)");
    }
    @Test
    public void getSupplier_returnStatus200() {
        PageableDto response = testClient
                .get()
                .uri("/api/v1/labor_types")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getTotalPages()).isEqualTo(1);
        List<Map<String, Object>> expectedList = List.of(
                Map.of(
                        "cost", 10.0,
                        "id", 10,
                        "typeLabor", "bordado"
                )
        );

        Assertions.assertThat(response.getContent()).isEqualTo(expectedList);
    }

    @Test
    public void editSupplier_dataValid_returnStatus200() {
        LaborTypesResponseDTO response = testClient
                .put()
                .uri("/api/v1/labor_types/{id}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LaborTypesResponseDTO("fornecedory", new BigDecimal(10), 10L))
                .exchange()
                .expectStatus().isOk()
                .expectBody(LaborTypesResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getTypeLabor()).isEqualTo("fornecedory");
    }

    @Test
    public void editSupplier_dataValid_returnStatus404() {

        ErrorMessage response = testClient
                .put()
                .uri("/api/v1/labor_types/{id}", this.id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LaborTypesCreateDTO("fornecedory", new BigDecimal(10)))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getStatus()).isEqualTo(404);
        org.assertj.core.api.Assertions.assertThat(response.getMessage()).isEqualTo(this.notFoundById);
    }

    @Test
    public void editSupplier_dataValid_returnStatus400() {

        ErrorMessage response = testClient
                .put()
                .uri("/api/v1/labor_types/{id}", 1011)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("teste")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void deleteSupplier_dataValid_returnStatus202() {

        testClient
                .delete()
                .uri("/api/v1/labor_types/{id}", 10)
                .exchange()
                .expectStatus().isAccepted();

    }

    @Test
    public void deleteSupplier_dataValid_returnStatus404() {

        ErrorMessage response = testClient
                .delete()
                .uri("/api/v1/labor_types/{id}", this.id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getStatus()).isEqualTo(404);
        org.assertj.core.api.Assertions.assertThat(response.getMessage()).isEqualTo(this.notFoundById);
    }
}
