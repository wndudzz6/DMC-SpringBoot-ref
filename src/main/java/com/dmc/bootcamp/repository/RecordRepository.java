package com.dmc.bootcamp.repository;

import com.dmc.bootcamp.domain.AppUser;
import com.dmc.bootcamp.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record,Long> {
    List<Record> findRecordByAppUser(AppUser user);

    @Query(value = "SELECT * FROM record WHERE user_id = :userId", nativeQuery = true)
    List<Record> findRecordsByUserId(@Param("userId") String userId);

    // 사용자 ID와 날짜로 특정 날짜의 기록을 조회하는 메서드 추가
    // 수정된 메서드: LocalDate의 시작과 끝을 사용하여 날짜 범위 검색
    @Query("SELECT r FROM Record r WHERE r.appUser = :user AND r.recordDate BETWEEN :startOfDay AND :endOfDay")
    List<Record> findByAppUserAndRecordDate(@Param("user") AppUser user,
                                            @Param("startOfDay") LocalDateTime startOfDay,
                                            @Param("endOfDay") LocalDateTime endOfDay);
}