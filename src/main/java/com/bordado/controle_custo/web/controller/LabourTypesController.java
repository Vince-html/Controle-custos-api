package com.bordado.controle_custo.web.controller;

import com.bordado.controle_custo.entity.LaborTypes;
import com.bordado.controle_custo.repository.projection.LaborTypesProjection;
import com.bordado.controle_custo.service.LaborTypesService;
import com.bordado.controle_custo.web.dto.PageableDto;
import com.bordado.controle_custo.web.dto.labortypes.LaborTypesCreateDTO;
import com.bordado.controle_custo.web.dto.labortypes.LaborTypesResponseDTO;
import com.bordado.controle_custo.web.dto.mapper.LaborTypesMapper;
import com.bordado.controle_custo.web.dto.mapper.PageableMapper;
import com.bordado.controle_custo.web.dto.rawmaterial.RawMaterialResponseDTO;
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
@Tag(name="Tipos de mão de obra", description = "Controle dos tipos de mão de obra")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/labor_types")
public class LabourTypesController {

    private final LaborTypesService laborTypesService;

    @Operation(summary = "Adiciona tipos de mão de obra ao banco de dados.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LaborTypesResponseDTO.class))),
                    @ApiResponse(responseCode= "404",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            responseCode= "422",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<LaborTypesResponseDTO> create (@RequestBody @Valid LaborTypesCreateDTO createDTO) {
        LaborTypes toLabor = LaborTypesMapper.toLaborType(createDTO);
        LaborTypes laborTypes = laborTypesService.salvar(toLabor);
        return ResponseEntity.status(HttpStatus.CREATED).body(LaborTypesMapper.toDto(laborTypes));
    }

    @Operation(summary = "Lista Tipos de mão de obra", description = "Lista os tipos de mão de obra no banco de dados.",
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
                                    schema = @Schema(implementation = LaborTypesResponseDTO.class))),
                    @ApiResponse(responseCode= "403",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

            })
    @GetMapping()
    public ResponseEntity<PageableDto> getAll(@Parameter(hidden = true) Pageable pageable) {
        Page<LaborTypesProjection> laborTypes = laborTypesService.findAll(pageable);

        return ResponseEntity.ok(PageableMapper.toDto(laborTypes));
    };


    @Operation(
            summary = "Atualizar Mão de obra",
            description = "Atualiza os tipos de mão de obra existentes",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "id",
                            required = true,
                            description = "ID do fornecedor a ser atualizado.",
                            content = @Content(schema = @Schema(type = "integer", example = "123"))
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Fornecedor atualizado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LaborTypesResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Fornecedor não encontrado.",
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
    public ResponseEntity<LaborTypesResponseDTO> update(@PathVariable Long id,
                                                        @RequestBody LaborTypesResponseDTO updateDTO) {
        LaborTypes laborUpdated = LaborTypesMapper.toUpdate(updateDTO);
        LaborTypes laborTypes = laborTypesService.update(id, laborUpdated);

        return ResponseEntity.ok().body(LaborTypesMapper.toDto(laborTypes));
    }

    @Operation(
            summary = "Deletar tipo de mão de obra",
            description = "Remove um tipo de mão de obra com base no ID informado.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "id",
                            required = true,
                            description = "ID do tipo de mão de obra a ser removido.",
                            content = @Content(schema = @Schema(type = "integer", example = "123"))
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Tipo de mão de obra removida com sucesso.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Acesso negado. O usuário não tem permissão para excluir fornecedores.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Fornecedor não encontrado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        laborTypesService.deleteById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
