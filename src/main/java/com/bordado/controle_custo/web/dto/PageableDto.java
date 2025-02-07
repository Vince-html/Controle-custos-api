package com.bordado.controle_custo.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PageableDto {
    private List content = new ArrayList<>();

    @JsonProperty("page")
    private int number;

    private int size;


    private int numberOfElements;

    private int totalPages;

    @JsonProperty("totalItens")
    private int totalElements;
}
