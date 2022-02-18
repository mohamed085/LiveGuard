package com.liveguard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChipDTO implements Serializable {
    private Long id;
    private String name;
    private String photo;
    private MultipartFile photoFile;
    private ChipTypeDTO chipType;
    private Long chipTypeId;
}
