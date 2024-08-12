package com.dmc.bootcamp.dto.request;

import com.dmc.bootcamp.domain.AppUser;
import com.dmc.bootcamp.domain.Food;
import com.dmc.bootcamp.domain.RecommendLog;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class RecommendLogRequest {
    private List<Food> foods;
    private AppUser appUser;
    private Long recommendId;

    public RecommendLog toEntity(){
        return RecommendLog.builder()
                .foods(foods)
                .appUser(appUser)
                .recommendId(recommendId)
                .build();
    }
}
