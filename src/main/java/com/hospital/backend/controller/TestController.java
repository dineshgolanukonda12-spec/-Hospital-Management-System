package com.hospital.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TestController {

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
            "status", "ONLINE",
            "message", "Hospital Management System Backend API is active and connected to MySQL!",
            "frontendApplicationUrl", "http://localhost:5173",
            "sampleEndpoints", Map.of(
                "dashboardStats", "/api/dashboard/stats",
                "doctors", "/api/doctors",
                "patients", "/api/patients",
                "appointments", "/api/appointments",
                "prescriptions", "/api/prescriptions"
            )
        );
    }

    @GetMapping("/api/test")
    public String test() {
        return "Hospital Management Backend is running!";
    }
}