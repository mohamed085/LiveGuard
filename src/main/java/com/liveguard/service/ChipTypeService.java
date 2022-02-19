package com.liveguard.service;

import com.liveguard.domain.ChipType;
import com.liveguard.dto.ChipTypeDTO;

import java.util.List;

public interface ChipTypeService {

    List<ChipTypeDTO> findAll();

    ChipTypeDTO findById(Long id);

    ChipTypeDTO add(ChipTypeDTO chipTypeDTO);
}
