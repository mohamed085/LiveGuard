package com.liveguard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liveguard.domain.UserSimpleData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChipAssociatedDetailsDTO {
    private String name;
    private String age;
    private String phone;
    private String photo;

    @JsonProperty("photo_file")
    private MultipartFile photoFile;

    @JsonProperty("add_by_user")
    private UserSimpleData addByUser;
}
