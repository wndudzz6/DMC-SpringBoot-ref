package com.dmc.bootcamp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateHealthStatusRequest {
    private LocalDate date;
    private  Float highBlood;
    private Float lowBlood;
    private Float emptySugar;
    private Float fullSugar;
    private Float weigh;
}
