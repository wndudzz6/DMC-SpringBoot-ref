package com.dmc.bootcamp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "recom_food", schema = "dmcc")
public class RecomFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    지연로딩 옵션 추가, 쓸데없는 컬럼 제거, JPA를 끄는 옵션 제거

    엔티티로 승격 / 객체로 교체
    * */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommend_id")
    private RecommendLog recommendLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    public void setRecommendLog(RecommendLog recommendLog) {
        this.recommendLog = recommendLog;
    }

    public void setFood(Food food) {
        this.food = food;
    }


}
