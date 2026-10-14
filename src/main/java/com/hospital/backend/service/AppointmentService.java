package com.hospital.backend.service;

import com.hospital.backend.entity.Appointment;
import com.hospital.backend.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getAppointmentsByPatient(String patient) {
        return appointmentRepository.findByPatient(patient);
    }

    public List<Appointment> getAppointmentsByDoctor(String doctor) {
        return appointmentRepository.findByDoctor(doctor);
    }

    public Appointment bookAppointment(Appointment appointment) {
        if (appointment.getAppointmentId() == null || appointment.getAppointmentId().trim().isEmpty()) {
            appointment.setAppointmentId("APT-" + (100 + (int) (Math.random() * 900)));
        }
        if (appointment.getStatus() == null || appointment.getStatus().trim().isEmpty()) {
            appointment.setStatus("Pending");
        }
        return appointmentRepository.save(appointment);
    }

    public Appointment updateStatus(Long id, String newStatus) {
        return appointmentRepository.findById(id).map(appt -> {
            appt.setStatus(newStatus);
            return appointmentRepository.save(appt);
        }).orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + id));
    }

    public Appointment updateStatusByAppointmentId(String appointmentId, String newStatus) {
        return appointmentRepository.findByAppointmentId(appointmentId).map(appt -> {
            appt.setStatus(newStatus);
            return appointmentRepository.save(appt);
        }).orElseThrow(() -> new IllegalArgumentException("Appointment not found with appointmentId: " + appointmentId));
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public void deleteByAppointmentId(String appointmentId) {
        appointmentRepository.findByAppointmentId(appointmentId).ifPresent(appointmentRepository::delete);
    }
}
