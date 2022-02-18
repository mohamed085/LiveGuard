package com.liveguard.service.serviceImp;

import com.liveguard.domain.Chip;
import com.liveguard.dto.ChipDTO;
import com.liveguard.exciptions.NotFoundException;
import com.liveguard.mapper.ChipMapper;
import com.liveguard.repository.ChipRepository;
import com.liveguard.service.ChipService;
import com.liveguard.service.ChipTypeService;
import com.liveguard.util.FileUploadUtil;
import com.liveguard.util.GenerateCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ChipServiceImp implements ChipService {

    private final ChipRepository chipRepository;
    private final ChipTypeService chipTypeService;

    public ChipServiceImp(ChipRepository chipRepository, ChipTypeService chipTypeService) {
        this.chipRepository = chipRepository;
        this.chipTypeService = chipTypeService;
    }

    @Override
    public List<ChipDTO> findAll() {
        log.debug("ChipService | findAll");
        List<Chip> chips = (List<Chip>) chipRepository.findAll();
        List<ChipDTO> chipDTOs = new ArrayList<>();
        chips.forEach(chip -> chipDTOs.add(ChipMapper.chipToChipDTO(chip)));
        return chipDTOs;
    }

    @Override
    public ChipDTO findById(Long id) {
        log.debug("ChipService | findById | id: " + id);
        Chip chip = chipRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("This chip not found"));

        return ChipMapper.chipToChipDTO(chip);
    }

    @Override
    public ChipDTO add(ChipDTO chipDTO) throws IOException {
        log.debug("ChipService | add | chipDTO: " + chipDTO.getName());
        Chip chip = ChipMapper.chipDTOToChip(chipDTO);

        chip.setChipType(chipTypeService.findById(chipDTO.getChipTypeId()));
        chip.setPassword(String.valueOf(GenerateCodeUtil.generateRandomDigits(12)));

        Chip savedChip = new Chip();

        if (!chipDTO.getPhotoFile().isEmpty()) {
            log.debug("ChipService | add | chipDTO has file");

            MultipartFile multipartFile = chipDTO.getPhotoFile();
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            log.debug("ChipService | add | file name: " + fileName);

            savedChip = chipRepository.save(chip);
            chip.setPhoto("/chip-photos/" + chip.getId() + "/" +fileName);
            savedChip = chipRepository.save(chip);

            String uploadDir = "chip-photos/" + savedChip.getId();

            log.debug("ChipService | add | savedUser : " + savedChip.toString());
            log.debug("ChipService | add | uploadDir : " + uploadDir);

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        } else {
            log.debug("ChipService | add | chipDTO not has file");

            savedChip = chipRepository.save(chip);
        }

        ChipDTO savedChipDTO = ChipMapper.chipToChipDTO(savedChip);
        return savedChipDTO;
    }

    @Override
    public List<ChipDTO> getChipsByType(Long chipTypeId) {
        log.debug("ChipService | getChipsByType | chipTypeId: " + chipTypeId);
        List<Chip> chips = chipRepository.findByChipTypeId(chipTypeId);
        List<ChipDTO> chipDTOs = new ArrayList<>();
        chips.forEach(chip -> chipDTOs.add(ChipMapper.chipToChipDTO(chip)));
        return chipDTOs;
    }
}
