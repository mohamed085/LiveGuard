package com.liveguard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChipDTO implements Serializable {
    private Long id;
    private String name;
    private String photo;

    @JsonProperty("photo_file")
    private MultipartFile photoFile;

    @JsonProperty("chip_type")
    private ChipTypeDTO chipType;

    @JsonProperty("chip_type_id")
    private Long chipTypeId;

    @JsonProperty("chip_associated_details")
    private ChipAssociatedDetailsDTO chipAssociatedDetails;

    private List<UserSimpleDataDTO> users;
}
