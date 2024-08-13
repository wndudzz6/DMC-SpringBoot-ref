package com.dmc.bootcamp.dto.response;

import com.dmc.bootcamp.domain.AppUser;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserResponse {
    private String userId;
   // private String password;
    private String userName;
    private String gender;
    private LocalDate birthday;
    private String diseaseInfo;
    private Double height;
    private Double weight;
    private  String address;
    private String email;
    private String phone;

    public UserResponse(AppUser user){
        this.userId=user.getUserId();
        this.userName=user.getUserName();
     //   this.password=user.getPassword();
        this.gender=user.getGender();
        this.birthday=user.getBirthday();
        this.diseaseInfo=user.getDiseaseInfo();
        this.height=user.getHeight();
        this.weight=user.getWeight();
        this.address=user.getAddress();
        this.email=user.getEmail();
        this.phone=user.getPhone();
    }
}
