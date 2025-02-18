package com.bordado.controle_custo;

import com.bordado.controle_custo.entity.SplitTime;
import com.bordado.controle_custo.entity.Worker;
import com.bordado.controle_custo.web.dto.PageableDto;
import com.bordado.controle_custo.web.dto.worker.WorkerCreateDTO;
import com.bordado.controle_custo.web.dto.worker.WorkerResponseDTO;
import com.bordado.controle_custo.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = "/sql/workers/workers-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/workers/workers-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkerIT {

    @Autowired
    WebTestClient testClient;

    public static WorkerCreateDTO getWorker(Long rawMaterialId, Long profitMarginId, Long laborTypeId, Worker.StatusWorker status) {

        Long idRawMaterial = Optional.ofNullable(rawMaterialId).orElse(6L);
        Long idProfitMargin = Optional.ofNullable(profitMarginId).orElse(10L);
        Long idLaborType = Optional.ofNullable(laborTypeId).orElse(10L);
        Worker.StatusWorker statusWorker = Optional.ofNullable(status).orElse(Worker.StatusWorker.EM_ANDAMENTO);
        List<Long> rawMaterialsIds = List.of(idRawMaterial);
        List<SplitTime> splitTimes = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        splitTimes.add(new SplitTime(
                LocalDateTime.parse("2025-01-20 21:48:41", formatter),
                LocalDateTime.parse("2025-01-20 11:48:41", formatter),
                0
        ));

        splitTimes.add(new SplitTime(
                LocalDateTime.parse("2025-01-21 22:00:00", formatter),
                LocalDateTime.parse("2025-01-21 12:00:00", formatter),
                43200
        ));

        splitTimes.add(new SplitTime(
                LocalDateTime.parse("2025-01-22 23:15:30", formatter),
                LocalDateTime.parse("2025-01-22 13:15:30", formatter),
                48600
        ));

        return new WorkerCreateDTO(rawMaterialsIds, idLaborType, idProfitMargin, statusWorker, 0, splitTimes);
    }

    @Test
    public void createWorker_dataValid_returnStatus200() {
        WorkerResponseDTO response = testClient
                .post()
                .uri("/api/v1/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getWorker(null, null, null, null))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(WorkerResponseDTO.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(Worker.StatusWorker.EM_ANDAMENTO);
    }

    static Stream<Arguments> invalidWorkerAndMessageProvider() {
        return Stream.of(
                Arguments.of(getWorker(130L, null, null, null), "Tipo de matéria-prima não existe"),
                Arguments.of(getWorker(null, 130L, null, null), "Tipo de margem de lucro não encontrado"),
                Arguments.of(getWorker(null, null, 130L, null), "Tipo de mão de obra não encontrada.")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidWorkerAndMessageProvider")
    public void createWorker_notFound_returnStatus404(WorkerCreateDTO workerCreateDTO, String expectedMessage) {
        // Realizando o POST com os parâmetros passados pelo CsvSource
        ErrorMessage response = testClient
                .post()
                .uri("/api/v1/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(workerCreateDTO)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo(expectedMessage);
    }
    @Test
    public void update_dataValid_returnStatus200() {
        WorkerResponseDTO response = testClient
                .put()
                .uri("/api/v1/workers/{id}", 4)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getWorker(null, null, null, Worker.StatusWorker.FINALIZADO))
                .exchange()
                .expectStatus().isOk()
                .expectBody(WorkerResponseDTO.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(Worker.StatusWorker.FINALIZADO);
    }


    @ParameterizedTest
    @MethodSource("invalidWorkerAndMessageProvider")
    public void update_notFound_returnStatu404(WorkerCreateDTO workerCreateDTO, String expectedMessage) {
        ErrorMessage response = testClient
                .put()
                .uri("/api/v1/workers/{id}", 4)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(workerCreateDTO)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo(expectedMessage);

    }

    @Test
    public void findWorkers_dataValid_returnStatus200() {
        PageableDto response = testClient
                .get()
                .uri("/api/v1/workers")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getSize()).isEqualTo(20);
    }

}
