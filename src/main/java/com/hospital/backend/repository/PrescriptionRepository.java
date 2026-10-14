package com.hospital.backend.repository;

import com.hospital.backend.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Optional<Prescription> findByPrescriptionId(String prescriptionId);
    List<Prescription> findByPatient(String patient);
    List<Prescription> findByDoctor(String doctor);
}
