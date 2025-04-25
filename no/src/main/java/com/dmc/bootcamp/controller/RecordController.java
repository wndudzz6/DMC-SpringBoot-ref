package com.dmc.bootcamp.controller;


import com.dmc.bootcamp.domain.AppUser;
import com.dmc.bootcamp.dto.request.RecordRequest;
import com.dmc.bootcamp.dto.response.RecordResponse;
import com.dmc.bootcamp.service.RecordService;
import com.dmc.bootcamp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.dmc.bootcamp.domain.Record;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class RecordController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final RecordService recordService;


    @PostMapping("/addRecord") // 식단 기록 추가
    public ResponseEntity<Record> addRecord(@RequestBody RecordRequest request) {
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // 인증된 사용자의 ID
        AppUser user = userService.findById(userId);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Record record = recordService.saveRecord(userId,request);
        return ResponseEntity.ok().body(record);
    }

    @GetMapping("/getAllRecord") //식단 기록 리스트를 모두 조회
    public ResponseEntity<List<RecordResponse>> getAll() {
        List<RecordResponse> list = recordService.getAllRecords().stream()
                .map(RecordResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(list);
    }

    //유저 Id에 따라 식단 기록 리스트를 얻어내기
    @GetMapping("/getAllRecordByUserId")
    public ResponseEntity<List<RecordResponse>> getAllRecordByUserId() {
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // 인증된 사용자의 ID
        AppUser user = userService.findById(userId);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<RecordResponse> list= recordService.getRecordsByUserId(userId).stream().map(RecordResponse:: new).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/getDetail/{recordId}") //식단 기록의 Id에 따라 조회
    public ResponseEntity<RecordResponse> getDetail(@PathVariable Long recordId) {
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // 인증된 사용자의 ID
        AppUser user = userService.findById(userId);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Record record = recordService.getRecordById(recordId);
        RecordResponse response = new RecordResponse(record);
        return ResponseEntity.ok().body(response);
    }

    //식단 기록 수정
    @PutMapping("/putRecord/{recordId}")
    public ResponseEntity<Record> putRecord(@PathVariable Long recordId, @RequestBody RecordRequest request) {
        Record record = recordService.getRecordById(recordId);
        if (record == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok().body(null);
    }

    //식단 기록을 삭제
    @DeleteMapping("/deleteRecord/{recordId}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long recordId) {
        recordService.deleteRecord(recordId);
        return ResponseEntity.ok().body(null);
    }

    // 날짜를 기반으로 특정 날짜의 기록을 조회하는 메서드 추가
    // http://localhost:8081/getRecordsByDate?date=2024-08-10
    @GetMapping("/getRecordsByDate")
    public ResponseEntity<List<RecordResponse>> getRecordsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // 인증된 사용자의 ID
        AppUser user = userService.findById(userId);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<RecordResponse> records = recordService.getRecordsByDate(userId, date).stream()
                .map(RecordResponse::new)
                .toList();

        return ResponseEntity.ok().body(records);
    }

}

