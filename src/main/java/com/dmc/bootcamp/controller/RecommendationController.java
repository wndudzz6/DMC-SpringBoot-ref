package com.dmc.bootcamp.controller;


import com.dmc.bootcamp.dto.response.RecommendCountFood;
import com.dmc.bootcamp.service.recommend.RecommendLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/recommend")
public class RecommendationController {
    @Autowired
    private RecommendLogService recommendLogService;

    @GetMapping("/by-date")
    public ResponseEntity<List<RecommendCountFood>> getRecommendCountByDate(@RequestParam("date") LocalDate date) {
        List<RecommendCountFood> recommendCountFoods = recommendLogService.getRecommendCountByDate(date);
        return ResponseEntity.ok().body(recommendCountFoods);
    }

}
