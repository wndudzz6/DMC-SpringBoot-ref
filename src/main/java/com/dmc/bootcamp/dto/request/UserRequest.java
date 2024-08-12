package com.dmc.bootcamp.dto.request;

import com.dmc.bootcamp.domain.AppUser;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserRequest {
    @NotEmpty
    private String userId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String userName;

    private String gender;

    private LocalDate birthday;

    private String diseaseInfo;

    private Double height;

    private Double weight;

    private  String address;

    private String phone;//이메일 추가
    private String email;//핸드폰 번호 추가

    public AppUser toEntity(){
        return AppUser.builder()
                .userId(userId)
                .userName(userName)
                .password(password)
                .gender(gender)
                .birthday(birthday)
                .diseaseInfo(diseaseInfo)
                .height(height)
                .weight(weight)
                .address(address)
                .phone(phone)
                .email(email)
                .build();
    }
}
