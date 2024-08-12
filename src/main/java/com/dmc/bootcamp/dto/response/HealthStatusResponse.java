package com.dmc.bootcamp.dto.response;

import com.dmc.bootcamp.domain.HealthStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class HealthStatusResponse {
    private  Float highBlood;
    private Float lowBlood;
    private Float emptySugar;
    private Float fullSugar;
    private String userId;
    private Float weigh;
    private LocalDateTime date;
    private long statusId;

    public HealthStatusResponse(HealthStatus healthStatus){
        this.statusId=healthStatus.getStatusId();
        this.highBlood=healthStatus.getHighBlood();
        this.lowBlood=healthStatus.getLowBlood();
        this.emptySugar=healthStatus.getEmptySugar();
        this.fullSugar=healthStatus.getFullSugar();
        this.userId=healthStatus.getAppUser().getUserId();
        this.date=healthStatus.getStatusTime();
        this.weigh=healthStatus.getWeigh();
    }
}
