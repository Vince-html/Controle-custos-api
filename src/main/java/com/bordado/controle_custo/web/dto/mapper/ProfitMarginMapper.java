package com.bordado.controle_custo.web.dto.mapper;

import com.bordado.controle_custo.entity.ProfitMargin;
import com.bordado.controle_custo.web.dto.profitMargin.ProfitMarginCreateDTO;
import com.bordado.controle_custo.web.dto.profitMargin.ProfitMarginResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfitMarginMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static ProfitMargin toProfit(ProfitMarginCreateDTO createDTO) {
        return modelMapper.map(createDTO, ProfitMargin.class);
    }

    public static ProfitMarginResponseDTO toDto(ProfitMargin profitMargin) {
        return modelMapper.map(profitMargin, ProfitMarginResponseDTO.class);
    }

    public static ProfitMargin toUpdate(ProfitMarginResponseDTO dto) {
        return modelMapper.map(dto, ProfitMargin.class);
    }
}
