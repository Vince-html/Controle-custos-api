package com.bordado.controle_custo.web.controller;



import com.bordado.controle_custo.entity.ProfitMargin;
import com.bordado.controle_custo.repository.projection.ProfitMarginProjection;
import com.bordado.controle_custo.service.ProfitMarginService;
import com.bordado.controle_custo.web.dto.PageableDto;

import com.bordado.controle_custo.web.dto.mapper.ProfitMarginMapper;
import com.bordado.controle_custo.web.dto.profitMargin.*;

import com.bordado.controle_custo.web.dto.mapper.PageableMapper;
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

@Slf4j
@Tag(name="Gerencia as Margem de lucro", description = "Controle das margens de lucros")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profit_margins")
public class ProfitMarginController {

    private final ProfitMarginService profitMarginService;

    @Operation(summary = "Adiciona margens de lucro ao banco de dados.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfitMarginResponseDTO.class))),
                    @ApiResponse(responseCode= "409",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            responseCode= "422",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<ProfitMarginResponseDTO> create (@RequestBody @Valid ProfitMarginCreateDTO createDTO) {
        ProfitMargin toLabor = ProfitMarginMapper.toProfit(createDTO);
        ProfitMargin laborTypes = profitMarginService.salvar(toLabor);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProfitMarginMapper.toDto(laborTypes));
    }

    @Operation(summary = "Lista as margens de lucro", description = "Lista as margens de lucros cadastradas no banco de dados.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name= "page", content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "valor inicial da pagina"),
                    @Parameter(in = ParameterIn.QUERY, name= "size", content = @Content(schema = @Schema(type = "integer", defaultValue = "10")),
                            description = "valor inicial do size"),
                    @Parameter(in = ParameterIn.QUERY, name= "sort", hidden = true, content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc")),
                            description = "Ordenação")
            },

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfitMarginResponseDTO.class))),
                    @ApiResponse(responseCode= "403",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

            })
    @GetMapping()
    public ResponseEntity<PageableDto> getAll(@Parameter(hidden = true) Pageable pageable) {
        Page<ProfitMarginProjection> laborTypes = profitMarginService.findAll(pageable);

        return ResponseEntity.ok(PageableMapper.toDto(laborTypes));
    };


    @Operation(
            summary = "Atualizar Margem de lucro",
            description = "Atualiza a margen de lucro existentes",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "id",
                            required = true,
                            description = "ID da margen de lucro a ser atualizado.",
                            content = @Content(schema = @Schema(type = "integer", example = "123"))
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Margem de lucro atualizada com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfitMarginResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Margem de lucro não encontrada.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos na requisição.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @PutMapping("{id}")
    public ResponseEntity<ProfitMarginResponseDTO> update(@PathVariable Long id,
                                                        @RequestBody ProfitMarginResponseDTO updateDTO) {
        ProfitMargin laborUpdated = ProfitMarginMapper.toUpdate(updateDTO);
        ProfitMargin laborTypes = profitMarginService.update(id, laborUpdated);

        return ResponseEntity.ok().body(ProfitMarginMapper.toDto(laborTypes));
    }

    @Operation(
            summary = "Deletar margem de lucro",
            description = "Remove uma margem de lucro com base no ID informado.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "id",
                            required = true,
                            description = "ID da margem de lucro a ser removido.",
                            content = @Content(schema = @Schema(type = "integer", example = "123"))
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Margem de lucro removida com sucesso.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Acesso negado. O usuário não tem permissão para excluir margem de lucro.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Margem de lucro não encontrado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        profitMarginService.deleteById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
