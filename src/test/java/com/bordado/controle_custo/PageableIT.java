package com.bordado.controle_custo;

import com.bordado.controle_custo.web.dto.PageableDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PageableIT {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();  // Inicializa uma nova instância do ObjectMapper antes de cada teste
    }

    @Test
    public void pageableDtoSerialization_deserializeCorrectly() throws Exception {
        String json = "{"
                + "\"content\": [{\"id\": 1, \"name\": \"Worker 1\"}],"
                + "\"page\": 1,"
                + "\"size\": 20,"
                + "\"numberOfElements\": 1,"
                + "\"totalPages\": 1,"
                + "\"totalItens\": 20"
                + "}";

        PageableDto response = objectMapper.readValue(json, PageableDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
        assertThat(response.getSize()).isEqualTo(20);
        assertThat(response.getNumber()).isEqualTo(1);
        assertThat(response.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    public void pageableDto_withWorkerDtoContent() {

        PageableDto response = new PageableDto();
        response.setContent(Arrays.asList(1,2));
        response.setNumber(1);
        response.setSize(20);
        response.setNumberOfElements(1);
        response.setTotalPages(1);
        response.setTotalElements(20);

        assertThat(response.getContent()).hasSize(2);

    }
}