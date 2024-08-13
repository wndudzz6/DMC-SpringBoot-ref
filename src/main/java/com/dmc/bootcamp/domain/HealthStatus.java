package com.dmc.bootcamp.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
@Table(name = "health_status")
public class HealthStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long statusId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @JsonBackReference
    private AppUser appUser;

    @Column(name = "status_time")
    private LocalDate statusTime;

    @Column(name = "high_blood")
    private Float highBlood;

    @Column(name = "low_blood")
    private Float lowBlood;

    @Column(name = "empty_sugar")
    private Float emptySugar;

    @Column(name = "full_sugar")
    private Float fullSugar;

    @Column(name = "weigh")
    private Float weigh;

    @Builder
    public HealthStatus(Float highBlood,Float lowBlood,Float emptySugar,Float fullSugar,AppUser user,Float weigh,LocalDate date){
        this.highBlood=highBlood;
        this.lowBlood=lowBlood;
        this.emptySugar=emptySugar;
        this.fullSugar=fullSugar;
        this.appUser=user;
        this.weigh=weigh;
        this.statusTime=date;
    }

//    @PrePersist
//    protected void onCreate() {
//        this.statusTime = LocalDateTime.now();
//    }

    public void update(Float highBlood, Float lowBlood, Float emptySugar, Float fullSugar, LocalDate date){
        this.highBlood=highBlood;
        this.lowBlood=lowBlood;
        this.emptySugar=emptySugar;
        this.fullSugar=fullSugar;
        this.statusTime=date;
    }
}
