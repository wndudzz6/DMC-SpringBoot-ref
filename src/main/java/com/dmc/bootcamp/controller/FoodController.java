package com.dmc.bootcamp.controller;

import com.dmc.bootcamp.domain.Food;

import com.dmc.bootcamp.domain.RecommendLog;
import com.dmc.bootcamp.dto.request.LikeRequest;
import com.dmc.bootcamp.dto.response.FoodResponse;
import com.dmc.bootcamp.dto.response.RecommendCountFood;
import com.dmc.bootcamp.service.FoodService;
import com.dmc.bootcamp.service.recommend.RecommendLogService;
import com.dmc.bootcamp.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;


import java.util.List;

//쿼리추가

@RestController
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;
    private final UserService userService;
    private final RecommendLogService recommendLogService;

    @PostConstruct
    public void init() {
        System.out.println("🚨 recommendLogService null? " + (recommendLogService == null));
    }

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
    //Test
    @GetMapping("/meal")
    public ResponseEntity<String> recommendMeal(HttpServletRequest request, Authentication authentication) {
        System.out.println("🔥 요청 URI: " + request.getRequestURI());
        System.out.println("🔥 실제 recommendMeal 핸들러 도착!");
        return ResponseEntity.ok("추천 OK!");
    }






//    @GetMapping("/recommend/{recommendId}")
//    public ResponseEntity<List<Food>> getFoodsByRecommendId(@PathVariable Long recommendId) {
//        List<Food> foods = foodService.getFoodsByRecommendId(recommendId);
//        if (foods.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(foods);
//    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    //자동완성
    @GetMapping("/api/foods/autocomplete")
    public List<String> autocomplete(@RequestParam String query) {
        return foodService.getAutocompleteSuggestions(query);
    }




}
