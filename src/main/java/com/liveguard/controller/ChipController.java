package com.liveguard.controller;

import com.liveguard.dto.ChipDTO;
import com.liveguard.dto.ChipTypeDTO;
import com.liveguard.payload.ApiResponse;
import com.liveguard.service.ChipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/chips")
public class ChipController {

    private final ChipService chipService;

    public ChipController(ChipService chipService) {
        this.chipService = chipService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllChips() {
        log.debug("ChipTypeController | getAllChipTypes");
        return ResponseEntity
                .ok()
                .body(chipService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChip(@PathVariable Long id) {
        log.debug("ChipTypeController | getChipType | id: " + id);
        return ResponseEntity
                .ok()
                .body(chipService.findById(id));
    }

    @RequestMapping(value = "",  method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public ResponseEntity<?> addChip(@ModelAttribute ChipDTO chipDTO) {
        log.debug("ChipTypeController | addChipType | chipTypeDTO: " + chipDTO.getName());

        try {
            ChipDTO returnChipDTO = chipService.add(chipDTO);
            return ResponseEntity
                    .ok()
                    .body(returnChipDTO);
        } catch (IOException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponse(false, "Failed to save chip photo"));
        }
    }



}
