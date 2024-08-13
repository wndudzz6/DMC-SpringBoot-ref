package com.dmc.bootcamp.dto.request;

import com.dmc.bootcamp.domain.AppUser;
import com.dmc.bootcamp.domain.HealthStatus;
import com.dmc.bootcamp.domain.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HealthStatusRequest {

    private Float highBlood;
    private Float lowBlood;
    private Float emptySugar;
    private Float fullSugar;
 //   private String userId;
    private Float weigh;
    private LocalDate date;

    public HealthStatus toEntity(AppUser user){
        return HealthStatus.builder()
                .highBlood(highBlood)
                .lowBlood(lowBlood)
                .emptySugar(emptySugar)
                .fullSugar(fullSugar)
                .weigh(weigh)
                .date(date)
                .user(user)
                .build();

    }
}
