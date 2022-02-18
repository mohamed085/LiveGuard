package com.liveguard.service;

import com.liveguard.domain.Chip;
import com.liveguard.domain.ChipType;
import com.liveguard.dto.ChipDTO;
import com.liveguard.dto.ChipTypeDTO;

import java.io.IOException;
import java.util.List;

public interface ChipService {

    List<ChipDTO> findAll();

    ChipDTO findById(Long id);

    ChipDTO add(ChipDTO chipDTO) throws IOException;
}
