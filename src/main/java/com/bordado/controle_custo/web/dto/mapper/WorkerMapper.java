package com.bordado.controle_custo.web.dto.mapper;

import com.bordado.controle_custo.entity.*;

import com.bordado.controle_custo.repository.projection.WorkerProjection;
import com.bordado.controle_custo.utils.CalculationTime;
import com.bordado.controle_custo.utils.CalculationTotalCost;
import com.bordado.controle_custo.web.dto.worker.SplitTimeResponseDTO;
import com.bordado.controle_custo.web.dto.worker.WorkerCreateDTO;
import com.bordado.controle_custo.web.dto.worker.WorkerResponseDTO;
import com.bordado.controle_custo.web.dto.worker.WorkerUpdateDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import java.util.stream.Collectors;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkerMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    static {

        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(WorkerCreateDTO.class, Worker.class).addMappings(mapper -> {
            mapper.skip(Worker::setId);
        });
    }

    public static Worker toWorker(WorkerCreateDTO createDTO, List<RawMaterial> rawMaterials, LaborTypes laborTypes, ProfitMargin profitMargin) {

        Worker worker = modelMapper.map(createDTO, Worker.class);

        worker.setLaborType(laborTypes);
        worker.setProfitMargin(profitMargin);
        worker.setRawMaterial(rawMaterials);

        return worker;
    }

    public  static WorkerResponseDTO toDto(Worker worker) {
        WorkerResponseDTO dto = new WorkerResponseDTO();
        dto.setId(worker.getId());
        dto.setRawMaterials(worker.getRawMaterial());
        dto.setLaborType(worker.getLaborType());
        dto.setProfitMargin(worker.getProfitMargin());
        dto.setStatus(worker.getStatus());


        // Mapeando a lista de SplitTime
        List<SplitTimeResponseDTO> splitTimeResponseDTOs = worker.getSplitTime().stream()
                .map(splitTime -> {
                    LocalDateTime initial = splitTime.getInitialTime();
                    LocalDateTime endTime = splitTime.getEndTime();
                    SplitTimeResponseDTO splitTimeDTO = new SplitTimeResponseDTO();
                    splitTimeDTO.setInitial(initial);
                    splitTimeDTO.setEnd(endTime);
                    splitTimeDTO.setTotalTime(CalculationTime.CalculationTotalSplitTime(initial, endTime));
                    return splitTimeDTO;
                })
                .collect(Collectors.toList());

        dto.setSplitTime(splitTimeResponseDTOs);

        return dto;
    }

    public  static Page<WorkerProjection> toPageToResponse(Page<WorkerProjection> workers) {
        List<WorkerResponseDTO> dtos = workers.getContent().stream()
                .map(worker -> {
                    WorkerResponseDTO dto = new WorkerResponseDTO();
                    dto.setId(worker.getId());

                    dto.setRawMaterials(worker.getRawMaterial());
                    dto.setLaborType(worker.getLaborType());
                    dto.setProfitMargin(worker.getProfitMargin());
                    dto.setStatus(worker.getStatus());

                    // Mapeamento de SplitTime (se necessário)
                    List<SplitTimeResponseDTO> splitTimeResponseDTOs = worker.getSplitTime().stream()
                            .map(splitTime -> {
                                LocalDateTime initial = splitTime.getInitialTime();
                                LocalDateTime endTime = splitTime.getEndTime();
                                SplitTimeResponseDTO splitTimeDTO = new SplitTimeResponseDTO();
                                splitTimeDTO.setInitial(initial);
                                splitTimeDTO.setEnd(endTime);
                                splitTimeDTO.setTotalTime(CalculationTime.CalculationTotalSplitTime(initial, endTime));
                                return splitTimeDTO;
                            })
                            .collect(Collectors.toList());

                    dto.setSplitTime(splitTimeResponseDTOs);

                    Integer totalTimeDedicated = CalculationTime.CalculationTotalDedicatedTime(worker.getSplitTime());
                    BigDecimal totalCost = CalculationTotalCost.CalculationTotalCostDedicated(
                            dto.getLaborType(),
                            dto.getProfitMargin(),
                            dto.getRawMaterials(),
                            totalTimeDedicated);

                    dto.setTotalCost(totalCost);
                    dto.setTimeDedicated(totalTimeDedicated);
                    return dto;
                })
                .collect(Collectors.toList());

        Pageable pageable = workers.getPageable(); // Mantém a paginação original
        return new PageImpl(dtos, pageable, workers.getTotalElements());
    }

    public static Worker toWorkerUpdate (WorkerUpdateDTO updateDTO, List<RawMaterial> rawMaterials, LaborTypes laborTypes, ProfitMargin profitMargin) {
        WorkerUpdateDTO dto = new WorkerUpdateDTO();
        dto.setId(updateDTO.getId());
        dto.setStatus(updateDTO.getStatus());

        // Mapeamento de SplitTime (se necessário)
        List<SplitTime> splitTimes = updateDTO.getSplitTime().stream()
                .map(splitTime -> {
                    LocalDateTime initial = splitTime.getInitialTime();
                    LocalDateTime endTime = splitTime.getEndTime();
                    SplitTime splitTimeDTO = new SplitTime();
                    splitTimeDTO.setInitialTime(initial);
                    splitTimeDTO.setEndTime(endTime);
                    splitTimeDTO.setTotalTime(CalculationTime.CalculationTotalSplitTime(initial, endTime));
                    return splitTimeDTO;
                })
                .collect(Collectors.toList());



        Worker worker = modelMapper.map(updateDTO, Worker.class);

        worker.setLaborType(laborTypes);
        worker.setProfitMargin(profitMargin);
        worker.setRawMaterial(rawMaterials);
        worker.setSplitTime(splitTimes);

        return worker;
    }
}
