package com.dmc.bootcamp.repository;

import com.dmc.bootcamp.domain.RecommendLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RecommendLogRepository extends JpaRepository<RecommendLog,Long> {

   @Query(value = "SELECT * FROM recommend_log r WHERE DATE(r.recom_time) = :date", nativeQuery = true)
   List<RecommendLog> findByDate(@Param("date") LocalDate date);

}
