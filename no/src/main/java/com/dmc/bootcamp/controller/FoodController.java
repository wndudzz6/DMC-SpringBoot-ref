package com.dmc.bootcamp.controller;

import com.dmc.bootcamp.domain.AppUser;
import com.dmc.bootcamp.domain.Food;

import com.dmc.bootcamp.domain.RecommendLog;
import com.dmc.bootcamp.dto.request.LikeRequest;
import com.dmc.bootcamp.dto.response.FoodResponse;
import com.dmc.bootcamp.dto.response.RecommendCountFood;
import com.dmc.bootcamp.service.FoodService;
import com.dmc.bootcamp.service.RecommendLogService;
import com.dmc.bootcamp.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

//쿼리추가

@RestController
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;
    private final UserService userService;
    private final RecommendLogService recommendLogService;

    @GetMapping("/food")
    public ResponseEntity<List<Food>> getListFood(){
        List<Food> list= foodService.getAllFood();
        return ResponseEntity.ok().body(list);
    }

    //호불호 컨트롤러
    @PostMapping("/api/like-meal/{recommendId}")
    public ResponseEntity<String> likeMeal(@PathVariable Long recommendId, @RequestBody LikeRequest likeRequest) {
        if (likeRequest.getRecommendId() == null || !likeRequest.getRecommendId().equals(recommendId)) {
            return ResponseEntity.badRequest().body("Invalid recommend ID in request.");
        }

        boolean result = recommendLogService.updateLikeStatus(recommendId, likeRequest.isLike());
        if (result) {
            return ResponseEntity.ok("Like status updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update like status.");
        }
    }


    @GetMapping("/recommend-meal")
    public ResponseEntity<RecommendCountFood> recommendMeal() {
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
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


    @GetMapping("/recommend/{recommendId}")
    public ResponseEntity<List<Food>> getFoodsByRecommendId(@PathVariable Long recommendId) {
        List<Food> foods = foodService.getFoodsByRecommendId(recommendId);
        if (foods.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(foods);
    }
    //자동완성
    @GetMapping("/api/foods/autocomplete")
    public List<String> autocomplete(@RequestParam String query) {
        return foodService.getAutocompleteSuggestions(query);
    }




}
