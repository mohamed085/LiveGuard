package com.liveguard.controller;

import com.liveguard.dto.ChipTypeDTO;
import com.liveguard.service.ChipTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chipTypes")
public class ChipTypeController {

    private final ChipTypeService chipTypeService;

    public ChipTypeController(ChipTypeService chipTypeService) {
        this.chipTypeService = chipTypeService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllChipTypes() {
        log.debug("ChipTypeController | getAllChipTypes");
        return ResponseEntity
                .ok()
                .body(chipTypeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChipType(@PathVariable Long id) {
        log.debug("ChipTypeController | getChipType | id: " + id);
        return ResponseEntity
                .ok()
                .body(chipTypeService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> addChipType(@RequestBody ChipTypeDTO chipTypeDTO) {
        log.debug("ChipTypeController | addChipType | chipTypeDTO: " + chipTypeDTO.toString());
        return ResponseEntity
                .ok()
                .body(chipTypeService.add(chipTypeDTO));
    }


}
