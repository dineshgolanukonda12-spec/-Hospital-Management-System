package com.hospital.backend.service;

import com.hospital.backend.repository.AppointmentRepository;
import com.hospital.backend.repository.DoctorRepository;
import com.hospital.backend.repository.PatientRepository;
import com.hospital.backend.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    public DashboardService(DoctorRepository doctorRepository,
                            PatientRepository patientRepository,
                            AppointmentRepository appointmentRepository,
                            PrescriptionRepository prescriptionRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        long totalDoctors = doctorRepository.count();
        long totalPatients = patientRepository.count();
        long totalAppointments = appointmentRepository.count();
        long totalPrescriptions = prescriptionRepository.count();
        long pendingAppointments = appointmentRepository.findByStatus("Pending").size();
        long confirmedAppointments = appointmentRepository.findByStatus("Confirmed").size();
        long completedAppointments = appointmentRepository.findByStatus("Completed").size();

        stats.put("totalDoctors", totalDoctors);
        stats.put("totalPatients", totalPatients);
        stats.put("totalAppointments", totalAppointments);
        stats.put("totalDepartments", 6);
        stats.put("totalPrescriptions", totalPrescriptions);
        stats.put("pendingAppointments", pendingAppointments);
        stats.put("confirmedAppointments", confirmedAppointments);
        stats.put("completedAppointments", completedAppointments);

        return stats;
    }
}
