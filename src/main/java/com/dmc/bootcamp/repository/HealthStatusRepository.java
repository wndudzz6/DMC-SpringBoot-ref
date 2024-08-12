package com.dmc.bootcamp.repository;

import com.dmc.bootcamp.domain.HealthStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.dmc.bootcamp.domain.AppUser;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface HealthStatusRepository extends JpaRepository<HealthStatus,Long> {

    //유저 Id에 따라 건강 관리 기록를 조회
    @Query(value = "select * from health_status where user_id = :userId",nativeQuery = true)
    List<HealthStatus> findHealthStatusByUserId(@Param("userId") String userId);

    //날짜에 따라 건강 관리 기록 조회
    @Query(value = "select * from health_status where status_time= :statusTime",nativeQuery = true)
    HealthStatus findHealthStatusByStatusTime(@Param("statusTime") Date statusTime);


    // 유저 ID랑 날짜에 따라 HealthStatus 조회
    @Query(value = "select * from health_status where user_id= :userId and status_time= :date",nativeQuery = true)
    HealthStatus findByUserAndDate(@Param("userId") String userId,@Param("date") LocalDate date);
}
