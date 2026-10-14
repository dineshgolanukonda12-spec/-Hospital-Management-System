package com.hospital.backend.service;

import com.hospital.backend.entity.Patient;
import com.hospital.backend.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public Patient addPatient(Patient patient) {
        if (patient.getPatientId() == null || patient.getPatientId().trim().isEmpty()) {
            patient.setPatientId("PAT-" + (100 + (int) (Math.random() * 900)));
        }
        if (patient.getRegisteredDate() == null) {
            patient.setRegisteredDate(LocalDate.now());
        }
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient details) {
        return patientRepository.findById(id).map(patient -> {
            patient.setName(details.getName());
            patient.setEmail(details.getEmail());
            patient.setPhone(details.getPhone());
            patient.setAge(details.getAge());
            patient.setGender(details.getGender());
            patient.setBloodGroup(details.getBloodGroup());
            patient.setAddress(details.getAddress());
            return patientRepository.save(patient);
        }).orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + id));
    }

    public Patient updatePatientByEmail(String email, Patient details) {
        return patientRepository.findByEmail(email).map(patient -> {
            if (details.getName() != null) patient.setName(details.getName());
            if (details.getPhone() != null) patient.setPhone(details.getPhone());
            if (details.getAge() != null) patient.setAge(details.getAge());
            if (details.getGender() != null) patient.setGender(details.getGender());
            if (details.getBloodGroup() != null) patient.setBloodGroup(details.getBloodGroup());
            if (details.getAddress() != null) patient.setAddress(details.getAddress());
            return patientRepository.save(patient);
        }).orElseGet(() -> {
            details.setEmail(email);
            return addPatient(details);
        });
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public void deleteByPatientId(String patientId) {
        patientRepository.findByPatientId(patientId).ifPresent(patientRepository::delete);
    }
}
