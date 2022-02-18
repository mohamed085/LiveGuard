package com.liveguard.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chips")
@Setter
@Getter
@NoArgsConstructor
public class Chip extends BaseEntity {

    private String name;
    private String photo;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chips_types",
            joinColumns = {@JoinColumn(name = "chip_id")},
            inverseJoinColumns = {@JoinColumn(name = "chip_type_id")}
    )
    private Set<ChipType> chipTypes = new HashSet<>();

    @ManyToMany(mappedBy = "chips")
    private Set<User> users = new HashSet<>();
}
