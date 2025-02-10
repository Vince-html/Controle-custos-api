package com.bordado.controle_custo.web.dto.mapper;

import com.bordado.controle_custo.entity.RawMaterial;
import com.bordado.controle_custo.entity.Supplier;
import com.bordado.controle_custo.web.dto.rawmaterial.RawMaterialCreateDTO;
import com.bordado.controle_custo.web.dto.rawmaterial.RawMaterialResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RawMaterialMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static RawMaterial toRawMaterial(RawMaterialCreateDTO createDTO, Supplier supplier) {
        RawMaterial rawMaterial = modelMapper.map(createDTO, RawMaterial.class);
        rawMaterial.setId(null);
        rawMaterial.setSupplier(supplier);
        return rawMaterial;
    }

    public static RawMaterialResponseDTO toDto(RawMaterial rawMaterial) {
        return modelMapper.map(rawMaterial, RawMaterialResponseDTO.class);
    }

    public static RawMaterial toUpdate(RawMaterialResponseDTO dto) {
        return modelMapper.map(dto, RawMaterial.class);
    }
}

