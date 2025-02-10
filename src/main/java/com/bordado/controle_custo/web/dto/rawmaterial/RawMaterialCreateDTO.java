package com.bordado.controle_custo.web.dto.rawmaterial;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterialCreateDTO {
    @NotBlank
    @Size(min=5, max=250)
    private String name;

    @Size(max=250)
    private String description;

    private Integer amount;

    private BigDecimal cost;

    @NotNull
    private Long idSupplier;

}
