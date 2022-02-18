package com.liveguard.service.serviceImp;

import com.liveguard.domain.ChipType;
import com.liveguard.dto.ChipTypeDTO;
import com.liveguard.exciptions.NotFoundException;
import com.liveguard.mapper.ChipTypeMapper;
import com.liveguard.repository.ChipTypeRepository;
import com.liveguard.service.ChipTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChipTypeServiceImp implements ChipTypeService {

    private final ChipTypeRepository chipTypeRepository;

    public ChipTypeServiceImp(ChipTypeRepository chipTypeRepository) {
        this.chipTypeRepository = chipTypeRepository;
    }

    @Override
    public List<ChipType> findAll() {
        log.debug("ChipTypeService | findAll");
        return (List<ChipType>) chipTypeRepository.findAll();
    }

    @Override
    public ChipType findById(Long id) {
        log.debug("ChipTypeService | findById | id: " + id);
        ChipType chipType = chipTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("This chip type not found"));
        return chipType;
    }

    @Override
    public ChipType add(ChipTypeDTO chipTypeDTO) {
        log.debug("ChipTypeService | add: " + chipTypeDTO.toString());
        ChipType chipType = ChipTypeMapper.chipTypeDTOToChipType(chipTypeDTO);
        return chipTypeRepository.save(chipType);
    }
}
