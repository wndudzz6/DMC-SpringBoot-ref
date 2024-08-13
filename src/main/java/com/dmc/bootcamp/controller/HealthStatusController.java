package com.dmc.bootcamp.controller;

import com.dmc.bootcamp.domain.AppUser;
import com.dmc.bootcamp.domain.HealthStatus;
import com.dmc.bootcamp.dto.request.HealthStatusRequest;
import com.dmc.bootcamp.dto.request.UpdateHealthStatusRequest;
import com.dmc.bootcamp.dto.response.HealthStatusResponse;
import com.dmc.bootcamp.service.HealthStatusService;
import com.dmc.bootcamp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/healthStatus")
public class HealthStatusController {

    private final HealthStatusService healthStatusService;
    private final UserService userService;

    @PostMapping("/saveOrUpdate") // Create new health status
    public ResponseEntity<?> createOrUpdateHealthStatus(@RequestBody HealthStatusRequest request) {
        try {
            JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String userId = auth.getName(); // 인증된 사용자의 ID

            AppUser user = userService.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            HealthStatus healthStatus = healthStatusService.saveOrUpdate(userId, request);
            return ResponseEntity.status(HttpStatus.OK).body(healthStatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating health status: " + e.getMessage());
        }
    }

    @GetMapping // Get all health statuses
    public ResponseEntity<List<HealthStatusResponse>> getAllHealthStatuses() {
        List<HealthStatusResponse> healthStatusResponses = healthStatusService.getAll().stream()
                .map(HealthStatusResponse::new)
                .toList();
        return ResponseEntity.ok(healthStatusResponses);
    }

    @GetMapping("/user") // Get health statuses by user ID
    public ResponseEntity<List<HealthStatusResponse>> getHealthStatusByUserId() {
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // 인증된 사용자의 ID

        AppUser user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<HealthStatusResponse> healthStatusResponses = healthStatusService.getAllStatusByUserId(userId).stream()
                .map(HealthStatusResponse::new)
                .toList();
        return ResponseEntity.ok().body(healthStatusResponses);
    }

    @DeleteMapping("/{id}") // Delete health status
    public ResponseEntity<Void> deleteHealthStatus(@PathVariable Long id) {
        healthStatusService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}") // Update health status
    public ResponseEntity<HealthStatus> updateHealthStatus(@PathVariable long id, @RequestBody UpdateHealthStatusRequest request) {
        HealthStatus updatedHealthStatus = healthStatusService.update(id, request);
        return ResponseEntity.ok(updatedHealthStatus);
    }
}
