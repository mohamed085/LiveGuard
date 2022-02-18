package com.liveguard.service.serviceImp;

import com.liveguard.domain.Chip;
import com.liveguard.domain.ChipAssociatedDetails;
import com.liveguard.domain.User;
import com.liveguard.dto.ChipAssociatedDetailsDTO;
import com.liveguard.dto.ChipDTO;
import com.liveguard.exciptions.NotFoundException;
import com.liveguard.mapper.ChipAssociatedDetailsMapper;
import com.liveguard.mapper.ChipMapper;
import com.liveguard.repository.ChipAssociatedDetailsRepository;
import com.liveguard.repository.ChipRepository;
import com.liveguard.service.ChipService;
import com.liveguard.service.ChipTypeService;
import com.liveguard.service.UserService;
import com.liveguard.util.FileUploadUtil;
import com.liveguard.util.GenerateCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserService userService;
    private final ChipAssociatedDetailsRepository chipAssociatedDetailsRepository;

    public ChipServiceImp(ChipRepository chipRepository, ChipTypeService chipTypeService, UserService userService, ChipAssociatedDetailsRepository chipAssociatedDetailsRepository) {
        this.chipRepository = chipRepository;
        this.chipTypeService = chipTypeService;
        this.userService = userService;
        this.chipAssociatedDetailsRepository = chipAssociatedDetailsRepository;
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

        ChipDTO chipDTO = ChipMapper.chipToChipDTO(chip);
        return chipDTO;
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
            chip.setPhoto("/chip-photos/" + savedChip.getId() + "/" +fileName);
            savedChip = chipRepository.save(chip);

            String uploadDir = "chip-photos/" + savedChip.getId();

            log.debug("ChipService | add | savedChip : " + savedChip.toString());
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

    @Override
    public ChipAssociatedDetailsDTO addChipAssociatedDetails(Long chipId, ChipAssociatedDetailsDTO chipAssociatedDetailsDTO) throws IOException {
        log.debug("ChipService | addChipAssociatedDetails | chipId: " + chipId);
        log.debug("ChipService | addChipAssociatedDetails | chipAssociatedDetails: " + chipAssociatedDetailsDTO.getName());

        log.debug("ChipService | addChipAssociatedDetails | get user authenticated account");
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        log.debug("ChipService | addChipAssociatedDetails | user email: " + userEmail);
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() ->  new NotFoundException("This email not exist"));

        Chip chip = chipRepository.findById(chipId)
                .orElseThrow(() -> new NotFoundException("This chip not found"));
        log.debug("ChipService | addChipAssociatedDetails | chip: " + chip.getName());

        ChipAssociatedDetails chipAssociatedDetails = ChipAssociatedDetailsMapper.ChipAssociatedDetailsDTOToChipAssociatedDetails(chipAssociatedDetailsDTO);
        chipAssociatedDetails.setAddByUser(user);

        ChipAssociatedDetails savedChipAssociatedDetails;
        if (!chipAssociatedDetailsDTO.getPhotoFile().isEmpty()) {
            log.debug("ChipService | addChipAssociatedDetails | chipAssociatedDetailsDTO has file");

            MultipartFile multipartFile = chipAssociatedDetailsDTO.getPhotoFile();
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            log.debug("ChipService | addChipAssociatedDetails | file name: " + fileName);

            savedChipAssociatedDetails = chipAssociatedDetailsRepository.save(chipAssociatedDetails);
            chipAssociatedDetails.setPhoto("/chip-associated_details-photos/" + savedChipAssociatedDetails.getId() + "/" +fileName);
            savedChipAssociatedDetails = chipAssociatedDetailsRepository.save(chipAssociatedDetails);

            String uploadDir = "chip-associated_details-photos/" + savedChipAssociatedDetails.getId();

            log.debug("ChipService | addChipAssociatedDetails | savedChipAssociatedDetails : " + savedChipAssociatedDetails.toString());
            log.debug("ChipService | addChipAssociatedDetails | uploadDir : " + uploadDir);

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        } else {
            log.debug("ChipService | addChipAssociatedDetails | savedChipAssociatedDetails not has file");

            savedChipAssociatedDetails = chipAssociatedDetailsRepository.save(chipAssociatedDetails);
        }

        chip.setChipAssociatedDetails(savedChipAssociatedDetails);
        chipRepository.save(chip);

        return ChipAssociatedDetailsMapper.chipAssociatedDetailsToChipAssociatedDetailsDTO(savedChipAssociatedDetails);
    }
}
