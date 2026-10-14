package com.hospital.backend.repository;

import com.hospital.backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByAppointmentId(String appointmentId);
    List<Appointment> findByPatient(String patient);
    List<Appointment> findByDoctor(String doctor);
    List<Appointment> findByStatus(String status);
    void deleteByAppointmentId(String appointmentId);
}
