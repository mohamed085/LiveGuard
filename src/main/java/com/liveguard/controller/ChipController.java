package com.liveguard.controller;

import com.liveguard.dto.ChipAssociatedDetailsDTO;
import com.liveguard.dto.ChipDTO;
import com.liveguard.payload.ApiResponse;
import com.liveguard.service.ChipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/chipType/{chipTypeId}")
    public ResponseEntity<?> getChipsByType(@PathVariable("chipTypeId") Long chipTypeId) {

        log.debug("ChipTypeController | getChipsByType | chipTypeId: " + chipTypeId);
        return ResponseEntity
                .ok()
                .body(chipService.getChipsByType(chipTypeId));
    }

    @RequestMapping(value = "/{id}/chip_associated_details",  method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<?> addChipAssociatedDetails(@PathVariable("id") Long id,
                                                      @ModelAttribute ChipAssociatedDetailsDTO chipAssociatedDetailsDTO) {

        log.debug("ChipTypeController | addChipAssociatedDetails | chip id: " + id);
        log.debug("ChipTypeController | addChipAssociatedDetails | chipAssociatedDetailsDTO: " + chipAssociatedDetailsDTO.getName());

        try {
            ChipAssociatedDetailsDTO savedChipAssociatedDetailsDTO = chipService.addChipAssociatedDetails(id, chipAssociatedDetailsDTO);
            return ResponseEntity
                    .ok()
                    .body(savedChipAssociatedDetailsDTO);
        } catch (IOException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponse(false, "Failed to save chip photo"));
        }
    }


}
