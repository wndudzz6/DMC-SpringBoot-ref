package com.dmc.bootcamp.service.recommend;

import com.dmc.bootcamp.domain.AppUser;
import com.dmc.bootcamp.domain.Food;
import com.dmc.bootcamp.domain.RecomFood;
import com.dmc.bootcamp.domain.RecommendLog;
import com.dmc.bootcamp.dto.response.FoodResponse;
import com.dmc.bootcamp.dto.response.RecommendCountFood;
import com.dmc.bootcamp.repository.RecomFoodRepository;
import com.dmc.bootcamp.repository.RecommendLogRepository;
import com.dmc.bootcamp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecommendLogService {

    RecommendLogRepository recommendLogRepository;
    RecomFoodRepository recomFoodRepository;
    RecommendationFactory recommendationFactory;



    private final UserRepository userRepository;

    @Autowired
    public RecommendLogService(RecommendLogRepository recommendLogRepository,
                               RecomFoodRepository recomFoodRepository, UserRepository userRepository) {
        this.recommendLogRepository = recommendLogRepository;
        this.recomFoodRepository = recomFoodRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RecommendLog saveRecommendationLog(String userId, List<Food> foods) {
        AppUser user = findUserOrThrow(userId); // 유틸로 분리 가능
        RecommendLog log = recommendationFactory.createLog(user);
        recommendLogRepository.save(log);

        List<RecomFood> recomFoods = recommendationFactory.createRecomFoods(log, foods);
        recomFoodRepository.saveAll(recomFoods);

        return log;
    }
    private AppUser findUserOrThrow(String userId) {
        AppUser user = userRepository.findUserByUserId(userId);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }


    //호불호 조사
        @Transactional
        public boolean updateLikeStatus(Long recommendId, boolean like) {
            RecommendLog log = recommendLogRepository.findById(recommendId)
                    .orElseThrow(() -> new EntityNotFoundException("RecommendLog not found"));

            log.setLikeStatus(like);
            recommendLogRepository.save(log);
            return true;
        }


    @Transactional
    public List<RecommendCountFood> getRecommendCountByDate(LocalDate date) {
        List<RecommendLog> logs = recommendLogRepository.findByDate(date);
        List<RecommendCountFood> recommendCountFoods = new ArrayList<>();

        for (RecommendLog log : logs) {
            List<Food> foods = recomFoodRepository.findFoodsByRecommendId(log.getRecommendId());
            int batchSize = 4;
            for (int i = 0; i < foods.size(); i += batchSize) {
                List<Food> mealFoods = foods.subList(i, Math.min(i + batchSize, foods.size()));
                RecommendCountFood recommendCountFood = new RecommendCountFood(
                        mealFoods.stream().map(FoodResponse::new).collect(Collectors.toList()),
                        log.getRecommendId() // 여기에 recommendId를 추가
                );
                recommendCountFoods.add(recommendCountFood);
            }
        }

        return recommendCountFoods;
    }

}

