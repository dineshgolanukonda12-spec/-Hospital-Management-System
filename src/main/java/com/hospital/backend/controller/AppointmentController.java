package com.hospital.backend.controller;

import com.hospital.backend.entity.Appointment;
import com.hospital.backend.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:5173")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<Appointment> getAllAppointments(@RequestParam(required = false) String patient,
                                                @RequestParam(required = false) String doctor) {
        if (patient != null && !patient.trim().isEmpty()) {
            return appointmentService.getAppointmentsByPatient(patient);
        }
        if (doctor != null && !doctor.trim().isEmpty()) {
            return appointmentService.getAppointmentsByDoctor(doctor);
        }
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Appointment> bookAppointment(@RequestBody Appointment appointment) {
        Appointment saved = appointmentService.bookAppointment(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        String newStatus = statusMap.get("status");
        try {
            Appointment updated = appointmentService.updateStatus(id, newStatus);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/code/{appointmentId}/status")
    public ResponseEntity<?> updateStatusByCode(@PathVariable String appointmentId, @RequestBody Map<String, String> statusMap) {
        String newStatus = statusMap.get("status");
        try {
            Appointment updated = appointmentService.updateStatusByAppointmentId(appointmentId, newStatus);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/code/{appointmentId}")
    public ResponseEntity<Void> deleteByCode(@PathVariable String appointmentId) {
        appointmentService.deleteByAppointmentId(appointmentId);
        return ResponseEntity.noContent().build();
    }
}
