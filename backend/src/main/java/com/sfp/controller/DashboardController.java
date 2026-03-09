package com.sfp.controller;

import com.sfp.dto.response.DashboardSummaryDTO;
import com.sfp.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary(@RequestParam Long userId) {
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/period")
    public ResponseEntity<DashboardSummaryDTO> getDashboardByPeriod(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        DashboardSummaryDTO summary = dashboardService.getDashboardByPeriod(userId, startDate, endDate);
        return ResponseEntity.ok(summary);
    }
}