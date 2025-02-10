package com.bordado.controle_custo.web.controller;


import com.bordado.controle_custo.entity.RawMaterial;
import com.bordado.controle_custo.repository.projection.RawMaterialProjection;
import com.bordado.controle_custo.service.RawMaterialService;
import com.bordado.controle_custo.web.dto.PageableDto;
import com.bordado.controle_custo.web.dto.mapper.PageableMapper;
import com.bordado.controle_custo.web.dto.mapper.RawMaterialMapper;
import com.bordado.controle_custo.web.dto.rawmaterial.RawMaterialCreateDTO;
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
@Tag(name="Matéria Prima", description = "Controle de matéria prima.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/raw_materials")
public class RawMaterialController {
    private final RawMaterialService rawMaterialService;

    @Operation(summary = "Adiciona a materia prima ao banco de dados.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RawMaterialResponseDTO.class))),
                    @ApiResponse(responseCode= "404",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            responseCode= "422",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<RawMaterialResponseDTO> create (@RequestBody @Valid RawMaterialCreateDTO createDTO) {
        RawMaterial savedRawMaterial = rawMaterialService.save(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(RawMaterialMapper.toDto(savedRawMaterial));
    }

    @Operation(summary = "Buscar matérias primas.", description = "Busca as matérias primas em estoque.",
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
                                    schema = @Schema(implementation = RawMaterialResponseDTO.class))),
                    @ApiResponse(responseCode= "403",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

            })
    @GetMapping()
    public ResponseEntity<PageableDto> getAll(@Parameter(hidden = true) Pageable pageable) {
        Page<RawMaterialProjection> rawMaterial = rawMaterialService.findAll(pageable);

        return ResponseEntity.ok(PageableMapper.toDto(rawMaterial));
    };


    @Operation(
            summary = "Atualizar material prima",
            description = "Atualiza matéria prima existente.",
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
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RawMaterialResponseDTO.class))
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
    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialResponseDTO> updateSupplier(
            @PathVariable Long id,
            @RequestBody RawMaterialResponseDTO updateDTO
    ) {
        RawMaterial updated = RawMaterialMapper.toUpdate(updateDTO);
        RawMaterial rawMaterial = rawMaterialService.updateRawMaterial(id, updated);
        return ResponseEntity.ok().body(RawMaterialMapper.toDto(rawMaterial));
    }
}
