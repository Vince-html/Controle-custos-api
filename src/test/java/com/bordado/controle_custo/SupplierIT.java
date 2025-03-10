package com.bordado.controle_custo;


import com.bordado.controle_custo.web.dto.PageableDto;
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

import java.util.List;
import java.util.Map;

@Sql(scripts = "/sql/suppliers/suppliers-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/suppliers/suppliers-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SupplierIT {

    int id = 11;
    String notFoundById = String.format("Fornecedor com ID %s não encontrado.", id);

    @Autowired
    WebTestClient testClient;

    @Test
    public void createSupplier_dataValid_returnStatus200() {
        SupplierResponseDTO response = testClient
                .post()
                .uri("/api/v1/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SupplierCreateDTO("Teste Fornecedor", "email@email.com"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SupplierResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getName()).isEqualTo("Teste Fornecedor");
        org.assertj.core.api.Assertions.assertThat(response.getContact()).isEqualTo("email@email.com");
    }

    @Test
    public void createSupplier_dataIsInvalid_returnStatus422() {
        ErrorMessage response = testClient
                .post()
                .uri("/api/v1/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SupplierCreateDTO("Teste Fornecedor", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getStatus()).isEqualTo(422);
        org.assertj.core.api.Assertions.assertThat(response.getMessage()).isNotNull();
    }

    @Test
    public void createSupplier_dataValid_returnStatus409() {
        ErrorMessage response = testClient
                .post()
                .uri("/api/v1/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SupplierCreateDTO("fornecedorx", "email@live.com"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getStatus()).isEqualTo(409);
        org.assertj.core.api.Assertions.assertThat(response.getMessage()).isEqualTo("Fornecedor já cadastrado.");
    }

    @Test
    public void getSupplier_returnStatus200() {
        PageableDto response = testClient
                .get()
                .uri("/api/v1/suppliers")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getTotalPages()).isEqualTo(1);
        List<Map<String, Object>> expectedList = List.of(
                Map.of(
                        "contact", "email@live.com",
                        "id", 10,
                        "name", "fornecedorx"
                )
        );

        Assertions.assertThat(response.getContent()).isEqualTo(expectedList);
    }

    @Test
    public void editSupplier_dataValid_returnStatus200() {
        SupplierResponseDTO response = testClient
                .put()
                .uri("/api/v1/suppliers/{id}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SupplierCreateDTO("fornecedory", "email@email.com"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(SupplierResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getName()).isEqualTo("fornecedory");
        org.assertj.core.api.Assertions.assertThat(response.getContact()).isEqualTo("email@email.com");
    }

    @Test
    public void editSupplier_dataValid_returnStatus404() {

        ErrorMessage response = testClient
                .put()
                .uri("/api/v1/suppliers/{id}", this.id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SupplierCreateDTO("fornecedory", "email@email.com"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getStatus()).isEqualTo(404);
        org.assertj.core.api.Assertions.assertThat(response.getMessage()).isEqualTo(this.notFoundById);
    }

    @Test
    public void editSupplier_dataValid_returnStatus422() {

        ErrorMessage response = testClient
                .put()
                .uri("/api/v1/suppliers/{id}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("teste")
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void deleteSupplier_dataValid_returnStatus202() {

       testClient
                .delete()
                .uri("/api/v1/suppliers/{id}", 10)
                .exchange()
                .expectStatus().isAccepted();

    }

    @Test
    public void deleteSupplier_dataValid_returnStatus404() {

        ErrorMessage response = testClient
                .delete()
                .uri("/api/v1/suppliers/{id}", this.id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getStatus()).isEqualTo(404);
        org.assertj.core.api.Assertions.assertThat(response.getMessage()).isEqualTo(this.notFoundById);
    }
}
