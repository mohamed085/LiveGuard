package com.liveguard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private String ringtone;
    private Double lat;
    private Double lng;
    private Double area;
    private Boolean mute;

    @JsonProperty("start_date")
    private Date startDate;

    @JsonProperty("end_date")
    private Date endDate;

    @JsonProperty("create_date")
    private Date createDate;

    @JsonProperty("repeat_id")
    private List<Long> repeatId;
    private List<TaskDayDTO> repeat;
    private ChipSimpleDataDTO chip;

    @JsonProperty("add_by_user")
    private UserSimpleDataDTO addByUser;
}
