package com.dmc.bootcamp.controller;


import com.dmc.bootcamp.domain.Food;
import com.dmc.bootcamp.domain.RecommendLog;
import com.dmc.bootcamp.dto.response.FoodResponse;
import com.dmc.bootcamp.dto.response.RecommendCountFood;
import com.dmc.bootcamp.service.FoodService;
import com.dmc.bootcamp.service.recommend.RecommendLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendLogService recommendLogService;
    private final FoodService foodService;

    @GetMapping("/by-date")
    public ResponseEntity<List<RecommendCountFood>> getRecommendCountByDate(@RequestParam("date") LocalDate date) {
        List<RecommendCountFood> recommendCountFoods = recommendLogService.getRecommendCountByDate(date);
        return ResponseEntity.ok().body(recommendCountFoods);
    }

    @PostMapping("/create-meal")
    public ResponseEntity<RecommendCountFood> recommendMeal(Authentication authentication) {
        String userId = authentication.getName();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<FoodResponse> list = foodService.getMeal(userId).stream().map(FoodResponse::new).toList();
        List<Food> recommendedFoods = foodService.getMeal(userId);

        RecommendLog recommendLog = recommendLogService.saveRecommendationLog(userId, recommendedFoods);
        Long recommendId = recommendLog.getRecommendId();  // 추천 식단 ID 취득

        RecommendCountFood recommendCountFood = new RecommendCountFood(list, recommendId);  // 수정된 생성자 호출

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return ResponseEntity.ok().headers(headers).body(recommendCountFood);
    }


}
