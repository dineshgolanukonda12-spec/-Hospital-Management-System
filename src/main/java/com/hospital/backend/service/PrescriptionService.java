package com.hospital.backend.service;

import com.hospital.backend.entity.Prescription;
import com.hospital.backend.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    public List<Prescription> getPrescriptionsByPatient(String patient) {
        return prescriptionRepository.findByPatient(patient);
    }

    public List<Prescription> getPrescriptionsByDoctor(String doctor) {
        return prescriptionRepository.findByDoctor(doctor);
    }

    public Prescription addPrescription(Prescription prescription) {
        if (prescription.getPrescriptionId() == null || prescription.getPrescriptionId().trim().isEmpty()) {
            prescription.setPrescriptionId("RX-" + (500 + (int) (Math.random() * 500)));
        }
        if (prescription.getDate() == null || prescription.getDate().trim().isEmpty()) {
            prescription.setDate(LocalDate.now().toString());
        }
        return prescriptionRepository.save(prescription);
    }
}
