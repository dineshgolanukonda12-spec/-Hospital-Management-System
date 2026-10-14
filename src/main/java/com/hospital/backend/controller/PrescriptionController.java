package com.hospital.backend.controller;

import com.hospital.backend.entity.Prescription;
import com.hospital.backend.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "http://localhost:5173")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    public List<Prescription> getAllPrescriptions(@RequestParam(required = false) String patient,
                                                  @RequestParam(required = false) String doctor) {
        if (patient != null && !patient.trim().isEmpty()) {
            return prescriptionService.getPrescriptionsByPatient(patient);
        }
        if (doctor != null && !doctor.trim().isEmpty()) {
            return prescriptionService.getPrescriptionsByDoctor(doctor);
        }
        return prescriptionService.getAllPrescriptions();
    }

    @PostMapping
    public ResponseEntity<Prescription> addPrescription(@RequestBody Prescription prescription) {
        Prescription saved = prescriptionService.addPrescription(prescription);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
