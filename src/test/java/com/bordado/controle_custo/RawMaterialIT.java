package com.bordado.controle_custo;


import com.bordado.controle_custo.entity.Supplier;
import com.bordado.controle_custo.web.dto.PageableDto;
import com.bordado.controle_custo.web.dto.rawmaterial.RawMaterialCreateDTO;
import com.bordado.controle_custo.web.dto.rawmaterial.RawMaterialResponseDTO;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Sql(scripts = "/sql/rawMaterial/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/rawMaterial/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RawMaterialIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createRawMaterial_dataValid_returnStatus200() {
        RawMaterialResponseDTO response = testClient
                .post()
                .uri("/api/v1/raw_materials")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new RawMaterialCreateDTO("Linha", "linha azul", 10, new BigDecimal(10), 10L))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RawMaterialResponseDTO.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Linha");
        assertThat(response.getDescription()).isEqualTo("linha azul");
    }

    @Test
    public void createRawMaterial_dataValid_returnStatus422() {
        ErrorMessage response = testClient
                .post()
                .uri("/api/v1/raw_materials")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new RawMaterialCreateDTO("", "linha azul", 10, new BigDecimal(10), 10L))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.getMessage()).isNotNull();
    }

    @Test
    public void createRawMaterial_dataValid_returnStatus404() {
        ErrorMessage response = testClient
                .post()
                .uri("/api/v1/raw_materials")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new RawMaterialCreateDTO("Linha", "linha azul", 10, new BigDecimal(10), 20L))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isNotNull();
        assertThat(response.getMessage()).isEqualTo("Fornecedor não encontrado com ID: 20");
    }

    @Test
    public void getRawMaterial_returnStatus200() {
        PageableDto response = testClient
                .get()
                .uri("/api/v1/raw_materials")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getTotalPages()).isEqualTo(1);
        List<Map<String, Object>> expectedList = List.of(
                Map.of(
                        "amount", 10,
                        "cost", 10.0,
                        "name", "bastidor",
                        "id", 6,
                        "description", "bastidor mdf",
                        "supplier", Map.of(
                                "contact", "email@live.com",
                                "id", 10,
                                "name", "fornecedorx"
                        )
                )
        );

        Assertions.assertThat(response.getContent()).isEqualTo(expectedList);
    }
    @Test
    public void editRawMaterial_dataValid_returnStatus200() {
        RawMaterialResponseDTO rawMaterial = new RawMaterialResponseDTO(
                "Linha verde",
                "linha de algodao verde",
                10,
                BigDecimal.ZERO,
                new SupplierResponseDTO(10L, "string", "string")
        );
        RawMaterialResponseDTO response = testClient
                .put()
                .uri("/api/v1/raw_materials/{id}", 6)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(rawMaterial)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RawMaterialResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getName()).isEqualTo("Linha verde");
        org.assertj.core.api.Assertions.assertThat(response.getDescription()).isEqualTo("linha de algodao verde");
    }
    @Test
    public void editRawMaterial_notFoundRawMaterial_returnStatus404() {
        RawMaterialResponseDTO rawMaterial = new RawMaterialResponseDTO(
                "Linha verde",
                "linha de algodao verde",
                10,
                BigDecimal.ZERO,
                new SupplierResponseDTO(10L, "string", "string")
        );
        ErrorMessage response = testClient
                .put()
                .uri("/api/v1/raw_materials/{id}", 123)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(rawMaterial)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("Matéria-prima com ID 123 não encontrada.");
    }

    @Test
    public void editRawMaterial_NotFoundSupplier_returnStatus404() {
        RawMaterialResponseDTO rawMaterial = new RawMaterialResponseDTO(
                "Linha verde",
                "linha de algodao verde",
                10,
                BigDecimal.ZERO,
                new SupplierResponseDTO(100L, "string", "string")
        );
        ErrorMessage response = testClient
                .put()
                .uri("/api/v1/raw_materials/{id}", 6)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(rawMaterial)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("Fornecedor com ID 100 não encontrado.");
    }
}
