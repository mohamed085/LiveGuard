package com.liveguard.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Setter
@Getter
@NoArgsConstructor
public class Task extends BaseEntity {
    private String name;
    private String description;
    private String ringtone;
    private Boolean mute;
    private Date startDate;
    private Date endDate;
    private Date createDate;
    private Double lat;
    private Double lng;
    private Double area;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "task_repeat",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "day_id")}
    )
    private Set<TaskDay> repeat = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User addByUser;

    @ManyToOne
    @JoinColumn(name = "chip_id")
    private Chip chip;

    public String getStartDateInString() {
        return String.valueOf(startDate);
    }

    public String getEndDateInString() {
        return String.valueOf(endDate);
    }

    public String getCreateDateInString() {
        return String.valueOf(createDate);
    }
}
