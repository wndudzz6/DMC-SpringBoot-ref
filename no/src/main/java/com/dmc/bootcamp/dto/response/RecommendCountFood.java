package com.dmc.bootcamp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class RecommendCountFood {

    private List<FoodResponse> foodResponseList;
    private Long recommendId;
//    private Map<String,Float> mapFood;

}
