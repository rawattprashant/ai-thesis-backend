package com.radiantSKill.infiniti.controllers;

import com.radiantSKill.infiniti.dto.*;
import com.radiantSKill.infiniti.services.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping("/stats")
    public DashboardStatsDTO stats(){

        return dashboardService.getStats();

    }

    @PostMapping("/students")
    public List<StudentDashboardDTO> students(
            @RequestBody DashboardFilterRequest filter,
            @RequestParam int page,
            @RequestParam int size
    ){

        return dashboardService.getStudents(filter,page,size);

    }

}