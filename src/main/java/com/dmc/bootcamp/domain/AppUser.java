package com.dmc.bootcamp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "user")
public class AppUser {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "disease_info")
    private String diseaseInfo;

    @Column(name = "height")
    private Double height;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "address")
    private String address;

    @Column(name = "role")
    private String role;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY)
    private List<HealthStatus> healthStatuses;

    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY)
    private List<RecommendLog> recommendLogs;

    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY)
    private List<Record> records;
}

