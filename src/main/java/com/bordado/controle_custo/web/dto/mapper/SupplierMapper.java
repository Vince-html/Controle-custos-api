package com.bordado.controle_custo.web.dto.mapper;

import com.bordado.controle_custo.entity.Supplier;
import com.bordado.controle_custo.web.dto.supplier.SupplierCreateDTO;
import com.bordado.controle_custo.web.dto.supplier.SupplierResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SupplierMapper {

    public static Supplier toSupplier(SupplierCreateDTO createDTO) {
        return new ModelMapper().map(createDTO, Supplier.class);
    }

    public  static SupplierResponseDTO toDto(Supplier supplier) {
        return new ModelMapper().map(supplier, SupplierResponseDTO.class);
    }

    public  static SupplierResponseDTO toDtoUpdate(Supplier supplier) {
        return new ModelMapper().map(supplier, SupplierResponseDTO.class);
    }

    public static Supplier toSupplierUpdate (SupplierResponseDTO updateDTO) {
        return new ModelMapper().map(updateDTO, Supplier.class);
    }
}
