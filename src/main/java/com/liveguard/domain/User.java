package com.liveguard.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String avatar;
    private String phone;
    private String address;
    private Date dob;
    private String postalCode;
    private Date createdTime;
    private String resetPasswordToken;
    private Boolean enable;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;
    private Boolean accountNonLocked;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private AuthenticationType authenticationType;

    @OneToOne(mappedBy = "user")
    private VerificationCode verificationCode;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chips_users",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "chip_id")}
    )
    private Set<Chip> chips = new HashSet<>();

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", enable=" + enable +
                ", password=" + password +
                ", roles=" + roles +
                ", authenticationType=" + authenticationType +
                '}';
    }
}

