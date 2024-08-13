package com.dmc.bootcamp.controller;

import com.dmc.bootcamp.domain.AppUser;
import com.dmc.bootcamp.dto.request.UserLoginRequest;
import com.dmc.bootcamp.dto.request.UserRequest;
import com.dmc.bootcamp.dto.response.UserResponse;
import com.dmc.bootcamp.repository.TokenBlacklistRepository;
import com.dmc.bootcamp.repository.UserRepository;
import com.dmc.bootcamp.service.UserService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.issuer}")
    private String issuer;


    private final UserRepository userRepository;


    private final AuthenticationManager authenticationManager;

    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final UserService userService;


    //유저 정보 가저오기
    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile(Authentication authentication) {
//        var response= new HashMap<String, Object>();
//        response.put("username", authentication.getName());
//        response.put("authorities", authentication.getAuthorities());
//
//        var appUser = userRepository.findUserByUserId(authentication.getName());
//        response.put("user", appUser);

        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // 인증된 사용자의 ID

        AppUser user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AppUser appUser1= userRepository.findUserByUserId(userId);
        UserResponse userResponse= new UserResponse(appUser1);

        return ResponseEntity.ok().body(userResponse);
    }


    //회원 가입
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid UserRequest registerDto, BindingResult result) {
        if(result.hasErrors()) {
            var errorList= result.getAllErrors();
            var errorsMap= new HashMap<String,String>();

            for(int i=0;i< errorList.size();i++){
                var error=(FieldError)errorList.get(i);
                errorsMap.put(error.getField(),error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }
        var bCrypt = new BCryptPasswordEncoder();
        AppUser appUser= new AppUser();
        appUser.setUserId(registerDto.getUserId());
        appUser.setPassword(bCrypt.encode(registerDto.getPassword()));
        appUser.setUserName(registerDto.getUserName());
        appUser.setDiseaseInfo(registerDto.getDiseaseInfo());
        appUser.setEmail(registerDto.getEmail());
        appUser.setPhone(registerDto.getPhone());
        appUser.setGender(registerDto.getGender());
        appUser.setBirthday(registerDto.getBirthday());
        appUser.setAddress(registerDto.getAddress());
        appUser.setHeight(registerDto.getHeight());
        appUser.setWeight(registerDto.getWeight());

        appUser.setRole("client");

        //사용자 존재성 확인
        try {
            //check if username/email are used or not
            var otherUser= userRepository.findUserByUserId(registerDto.getUserId());
            if(otherUser!=null){
                return ResponseEntity.badRequest().body("Username already used");
            }

//            otherUser= userRepository.find(registerDto.getEmail());
//            if(otherUser!=null){
//                return ResponseEntity.badRequest().body("Email already used");
//            }
            userRepository.save(appUser);

            String jwtToken= createJwtToken(appUser);

            var response= new HashMap<String,Object>();
            response.put("jwtToken",jwtToken);
            response.put("user",appUser);
            return ResponseEntity.ok(response);
        }
        catch (Exception ex){
            System.out.println("there is an exception: ");
            ex.printStackTrace();
        }

        return ResponseEntity.badRequest().body("Error registering user");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody UserLoginRequest loginDto, BindingResult result){
        if(result.hasErrors()) {
            var errorList= result.getAllErrors();
            var errorsMap= new HashMap<String,String>();
            for(int i=0;i< errorList.size();i++){
                var error=(FieldError)errorList.get(i);
                errorsMap.put(error.getField(),error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUserId(),
                            loginDto.getPassword()
                    )
            );

            AppUser appUser= userRepository.findByUserId(loginDto.getUserId());
            String jwtToken= createJwtToken(appUser);

           var response= new HashMap<String,Object>();
            response.put("jwtToken",jwtToken);
           // response.put("user",appUser);
            return ResponseEntity.ok().body(response);
        }
        catch (Exception ex){
            System.out.println("there is an exception: ");
            ex.printStackTrace();
        }
        return  ResponseEntity.badRequest().body("Bad username or password");


    }


    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request){
        if (request == null) {
            return ResponseEntity.badRequest().body("User is not authenticated");
        }

        String token = extractTokenFromAuthentication(request);

        if (token != null) {
            tokenBlacklistRepository.add(token);
            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }
    private String extractTokenFromAuthentication(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }





    private String createJwtToken(AppUser appUser){
        Instant now= Instant.now();

        JwtClaimsSet claims= JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds((24*3600)))
                .subject(appUser.getUserId())
                .claim("role",appUser.getRole())
                .build();

        var encoder= new NimbusJwtEncoder(
                new ImmutableSecret<>(secretKey.getBytes()));

        var param= JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),claims);

        return encoder.encode(param).getTokenValue();
    }
}
