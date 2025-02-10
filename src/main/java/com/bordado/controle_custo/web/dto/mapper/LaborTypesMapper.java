package com.bordado.controle_custo.web.dto.mapper;

import com.bordado.controle_custo.entity.LaborTypes;
import com.bordado.controle_custo.web.dto.labortypes.LaborTypesCreateDTO;
import com.bordado.controle_custo.web.dto.labortypes.LaborTypesResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LaborTypesMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static LaborTypes toLaborType(LaborTypesCreateDTO createDTO) {
        return modelMapper.map(createDTO, LaborTypes.class);

    }

    public static LaborTypesResponseDTO toDto(LaborTypes laborTypes) {
        return modelMapper.map(laborTypes, LaborTypesResponseDTO.class);
    }

    public static LaborTypes toUpdate(LaborTypesResponseDTO dto) {
        return modelMapper.map(dto, LaborTypes.class);
    }
}
