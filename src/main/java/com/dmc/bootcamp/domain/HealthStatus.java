package com.dmc.bootcamp.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private LocalDateTime statusTime;

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
    public HealthStatus(float highBlood,float lowBlood,float emptySugar,float fullSugar,AppUser user,Float weigh){
        this.highBlood=highBlood;
        this.lowBlood=lowBlood;
        this.emptySugar=emptySugar;
        this.fullSugar=fullSugar;
        this.appUser=user;
        this.weigh=weigh;
    }

    @PrePersist
    protected void onCreate() {
        this.statusTime = LocalDateTime.now();
    }

    public void update(Float highBlood,Float lowBlood,Float emptySugar,Float fullSugar){
        this.highBlood=highBlood;
        this.lowBlood=lowBlood;
        this.emptySugar=emptySugar;
        this.fullSugar=fullSugar;
    }
}
