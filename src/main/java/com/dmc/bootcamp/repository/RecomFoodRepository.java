package com.dmc.bootcamp.repository;

import com.dmc.bootcamp.domain.Food;
import com.dmc.bootcamp.domain.RecomFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecomFoodRepository extends JpaRepository<RecomFood, Long> {

    @Query("SELECT rf.food FROM RecomFood rf " +
            "WHERE rf.recommendLog.recommendId = :recommendId")
    List<Food> findFoodsByRecommendId(@Param("recommendId") Long recommendId);
}
