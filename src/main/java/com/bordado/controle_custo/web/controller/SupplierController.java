package com.bordado.controle_custo.web.controller;


import com.bordado.controle_custo.entity.Supplier;
import com.bordado.controle_custo.repository.projection.SupplierProjection;
import com.bordado.controle_custo.service.SupplierService;
import com.bordado.controle_custo.web.dto.PageableDto;
import com.bordado.controle_custo.web.dto.mapper.PageableMapper;
import com.bordado.controle_custo.web.dto.mapper.SupplierMapper;
import com.bordado.controle_custo.web.dto.supplier.SupplierCreateDTO;
import com.bordado.controle_custo.web.dto.supplier.SupplierResponseDTO;
import com.bordado.controle_custo.web.dto.supplier.SupplierUpdateDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name="Fornecedores", description = "Controle de fornecedores de matérial.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {
    private final SupplierService supplierService;



    @Operation(summary = "Adiciona um novo fornecedor ao banco de dados.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupplierResponseDTO.class))),
                    @ApiResponse(responseCode= "409",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            responseCode= "422",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<SupplierResponseDTO> create (@RequestBody @Valid SupplierCreateDTO createDTO) {
        Supplier supplier = SupplierMapper.toSupplier(createDTO);
        supplierService.salvar(supplier);

        return ResponseEntity.status(HttpStatus.CREATED).body(SupplierMapper.toDto(supplier));
    }

    @Operation(summary = "Buscar fornecedores",
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
                                    schema = @Schema(implementation = SupplierResponseDTO.class))),
                    @ApiResponse(responseCode= "403",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

            })
    @GetMapping()
    public ResponseEntity<PageableDto> getAll(@Parameter(hidden = true) Pageable pageable) {
        Page<SupplierProjection> suppliers = supplierService.findAll(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(suppliers));
    };

    @Operation(
            summary = "Deletar fornecedor pelo ID",
            description = "Remove um fornecedor com base no ID informado.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "id",
                            required = true,
                            description = "ID do fornecedor a ser removido.",
                            content = @Content(schema = @Schema(type = "integer", example = "123"))
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Fornecedor removido com sucesso.",
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
        supplierService.deleteById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(
            summary = "Atualizar fornecedor",
            description = "Atualiza o nome, contato ou ambos de um fornecedor existente.",
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
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SupplierResponseDTO.class))
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
    public ResponseEntity<SupplierResponseDTO> updateSupplier(
            @PathVariable Long id,
            @RequestBody SupplierUpdateDTO updateDTO
    ) {
        Supplier updated = SupplierMapper.toSupplierUpdate(updateDTO);
        Supplier supplier = supplierService.updateSupplier(id, updated);
        return ResponseEntity.ok().body(SupplierMapper.toDto(supplier));
    }


}
