package com.dmc.bootcamp.service;

import com.dmc.bootcamp.domain.AppUser;
import com.dmc.bootcamp.domain.HealthStatus;
import com.dmc.bootcamp.domain.AppUser;
import com.dmc.bootcamp.dto.request.HealthStatusRequest;
import com.dmc.bootcamp.dto.request.UpdateHealthStatusRequest;
import com.dmc.bootcamp.repository.HealthStatusRepository;
import com.dmc.bootcamp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthStatusService {
    private final HealthStatusRepository healthStatusRepository;
    private final UserRepository userRepository;

    //건강 상태 기록 저장
    public HealthStatus save(String userId,HealthStatusRequest request){
        AppUser user= userRepository.findUserByUserId(userId);
       return healthStatusRepository.save(request.toEntity(user));
    }

    //사용자 건강 상태 기록 조회
    public List<HealthStatus> getAll(){
        return healthStatusRepository.findAll();
    }

    public List<HealthStatus> getAllStatusByUserId(String userId) {
        return healthStatusRepository.findHealthStatusByUserId(userId);
    }


    //사용자 건강 상태 기록 삭제
    public void delete(long id){
        HealthStatus healthStatus= healthStatusRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found"+id));
        healthStatusRepository.delete(healthStatus);
    }


    //사용자 건강 상태 기록 업데이트
    @Transactional
    public HealthStatus update(long id, UpdateHealthStatusRequest request){
        HealthStatus healthStatus= healthStatusRepository.findById(id).orElseThrow(()->new IllegalArgumentException("not found: "+id));
        healthStatus.update(request.getHighBlood(), request.getLowBlood(), request.getEmptySugar(), request.getFullSugar());
        return healthStatus;
    }


    @Transactional
    public HealthStatus saveOrUpdate(String userId, HealthStatusRequest request) {
        AppUser user = userRepository.findUserByUserId(userId);
        LocalDate today = LocalDate.now();

        // 하루 유저는 건강 상태 기록했는지 안는지
        HealthStatus existingHealthStatus = healthStatusRepository.findByUserAndDate(userId,today);

        if (existingHealthStatus != null) {
            // 기준 기록 있으면 업데이트
            updateHealthStatus(existingHealthStatus, request);
            return healthStatusRepository.save(existingHealthStatus);
        } else {
            // 기준 기록 없으면 생성
            return healthStatusRepository.save(request.toEntity(user));
        }
    }

    // 건강 상태 기록을 업데이트
    private void updateHealthStatus(HealthStatus healthStatus, HealthStatusRequest request) {
        // Update fields directly based on request values
        if (request.getHighBlood() != null) {
            healthStatus.setHighBlood(request.getHighBlood());
        }
        if (request.getLowBlood() != null) {
            healthStatus.setLowBlood(request.getLowBlood());
        }
        if (request.getFullSugar() != null) {
            healthStatus.setFullSugar(request.getFullSugar());
        }
        if (request.getEmptySugar() != null) {
            healthStatus.setEmptySugar(request.getEmptySugar());
        }
        if (request.getWeigh() != null) {
            healthStatus.setWeigh(request.getWeigh());
        }
    }




}
