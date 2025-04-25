package com.dmc.bootcamp.domain;

import com.dmc.bootcamp.repository.RecomFoodRepository;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recommend_log")
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class RecommendLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendId;

    @Column(name = "recom_time")
    private LocalDateTime recomTime;

    @OneToMany(mappedBy = "recommendLog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RecomFood> recomFoods;


    @ManyToOne(fetch = FetchType.LAZY) //@XtoOne 형태 지연로딩으로 변경
    @JoinColumn(name = "userId")
    @JsonBackReference
    private AppUser appUser;


    @Column(name = "like_status")
    private boolean likeStatus;

    public RecommendLog(){};

    @PrePersist
    protected void onCreate() {
        this.recomTime = LocalDateTime.now();
    }

    @Builder
    public RecommendLog( List<Food> foods,AppUser appUser, boolean likeStatus, Long recommendId) {
        this.appUser=appUser;
        this.likeStatus=likeStatus;
        this.recommendId=recommendId;
    }
}
