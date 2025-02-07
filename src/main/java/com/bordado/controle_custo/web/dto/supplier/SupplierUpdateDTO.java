package com.bordado.controle_custo.web.dto.supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierUpdateDTO {
    private String name;
    private String contact;
}