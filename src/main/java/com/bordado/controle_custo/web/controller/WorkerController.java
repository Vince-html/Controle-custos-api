package com.bordado.controle_custo.web.controller;

import com.bordado.controle_custo.entity.SplitTime;
import com.bordado.controle_custo.entity.Worker;

import com.bordado.controle_custo.repository.projection.WorkerProjection;
import com.bordado.controle_custo.service.WorkerService;
import com.bordado.controle_custo.utils.CalculationTime;
import com.bordado.controle_custo.utils.CalculationTotalCost;
import com.bordado.controle_custo.web.dto.PageableDto;
import com.bordado.controle_custo.web.dto.mapper.PageableMapper;
import com.bordado.controle_custo.web.dto.mapper.WorkerMapper;
import com.bordado.controle_custo.web.dto.worker.SplitTimeResponseDTO;
import com.bordado.controle_custo.web.dto.worker.WorkerCreateDTO;
import com.bordado.controle_custo.web.dto.worker.WorkerResponseDTO;
import com.bordado.controle_custo.web.dto.worker.WorkerUpdateDTO;
import com.bordado.controle_custo.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Tag(name = "Trabalhos", description = "Controle de trabalhos realizados ou em andamento.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/workers")
public class WorkerController {
    private final WorkerService workerService;

    @Operation(summary = "Adiciona um novo trabalho ao banco de dados.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = WorkerResponseDTO.class))),
                    @ApiResponse(
                            responseCode = "422",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<WorkerResponseDTO> create(@RequestBody @Valid WorkerCreateDTO createDTO) {
        List<SplitTime> splitTime = createDTO.getSplitTime();
        for (SplitTime split : splitTime) {
            Integer totalDedicatedSplitTime = CalculationTime.CalculationTotalSplitTime(split.getInitialTime(), split.getEndTime());
            split.setTotalTime(totalDedicatedSplitTime);
        }
        createDTO.setSplitTime(splitTime);
        Worker workerSave = workerService.save(createDTO);

        Integer totalTimeDedicated = CalculationTime.CalculationTotalDedicatedTime(workerSave.getSplitTime());
        BigDecimal totalCost = CalculationTotalCost.CalculationTotalCostDedicated(
                workerSave.getLaborType(),
                workerSave.getProfitMargin(),
                workerSave.getRawMaterial(),
                totalTimeDedicated);

        WorkerResponseDTO workerResponseDTO = WorkerMapper.toDto(workerSave);
        workerResponseDTO.setTotalCost(totalCost);
        workerResponseDTO.setTimeDedicated(totalTimeDedicated);

        return ResponseEntity.status(HttpStatus.CREATED).body(workerResponseDTO);

    }

    @Operation(summary = "Buscar Trabalhos ativos e finalizados.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "page", content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "valor inicial da pagina"),
                    @Parameter(in = ParameterIn.QUERY, name = "size", content = @Content(schema = @Schema(type = "integer", defaultValue = "10")),
                            description = "valor inicial do size"),
                    @Parameter(in = ParameterIn.QUERY, name = "sort", hidden = true, content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc")),
                            description = "Ordenação")
            },

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = WorkerResponseDTO.class))),
                    @ApiResponse(responseCode = "403",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

            })
    @GetMapping()
    public ResponseEntity<PageableDto> getAll(@Parameter(hidden = true) Pageable pageable) {
        Page<WorkerProjection> workers = workerService.findAll(pageable);

        Page<WorkerProjection> workerResponseDTOS = WorkerMapper.toPageToResponse(workers);

        return ResponseEntity.ok(PageableMapper.toDto(workerResponseDTOS));
    }

    ;

    @Operation(summary = "Edita um trabalho.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = WorkerResponseDTO.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Fornecedor não encontrado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos na requisição.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "409",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            responseCode = "422",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))

            })
    @PutMapping("{id}")
    public ResponseEntity<WorkerResponseDTO> update(@PathVariable Long id, @RequestBody @Valid WorkerUpdateDTO updateDTO) {

        Worker workerUpdate = workerService.update(id, updateDTO);

        Integer totalTimeDedicated = CalculationTime.CalculationTotalDedicatedTime(workerUpdate.getSplitTime());
        BigDecimal totalCost = CalculationTotalCost.CalculationTotalCostDedicated(
                workerUpdate.getLaborType(),
                workerUpdate.getProfitMargin(),
                workerUpdate.getRawMaterial(),
                totalTimeDedicated);

        WorkerResponseDTO workerResponseDTO = WorkerMapper.toDto(workerUpdate);
        workerResponseDTO.setTotalCost(totalCost);
        workerResponseDTO.setTimeDedicated(totalTimeDedicated);

        return ResponseEntity.ok().body(workerResponseDTO);

    }

}
