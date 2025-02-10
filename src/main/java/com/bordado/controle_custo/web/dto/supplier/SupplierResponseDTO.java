package com.bordado.controle_custo.web.dto.supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierResponseDTO {
        private Long id;
        private String name;
        private String contact;
}
