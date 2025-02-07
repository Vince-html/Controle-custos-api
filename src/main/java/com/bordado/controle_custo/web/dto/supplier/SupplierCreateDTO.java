package com.bordado.controle_custo.web.dto.supplier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierCreateDTO {

    @NotBlank
    @Size(min=5, max=250)
    private String name;

    @NotBlank
    @Size(min=2, max=250)
    private String contact;
}
