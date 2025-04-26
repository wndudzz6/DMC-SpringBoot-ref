package com.dmc.bootcamp.service.recommend;

import com.dmc.bootcamp.domain.AppUser;
import com.dmc.bootcamp.domain.Food;
import com.dmc.bootcamp.domain.RecomFood;
import com.dmc.bootcamp.domain.RecommendLog;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecommendationFactory {

    @PostConstruct
    public void init() {
        System.out.println("✅ RecommendationFactory 등록됨");
    }

    public RecommendLog createLog(AppUser user) {
        RecommendLog log = new RecommendLog();
        log.setAppUser(user);
        log.setRecomTime(LocalDateTime.now());
        log.setLikeStatus(false);
        return log;
    }

    public List<RecomFood> createRecomFoods(RecommendLog log, List<Food> foods) {
        return foods.stream().map(food -> {
            RecomFood rf = new RecomFood();
            rf.setRecommendLog(log);
            rf.setFood(food);
            return rf;
        }).collect(Collectors.toList());
    }
}
