package com.hospital.backend.config;

import com.hospital.backend.entity.Appointment;
import com.hospital.backend.entity.Doctor;
import com.hospital.backend.entity.Patient;
import com.hospital.backend.entity.Prescription;
import com.hospital.backend.entity.User;
import com.hospital.backend.repository.AppointmentRepository;
import com.hospital.backend.repository.DoctorRepository;
import com.hospital.backend.repository.PatientRepository;
import com.hospital.backend.repository.PrescriptionRepository;
import com.hospital.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    public DataInitializer(UserRepository userRepository,
                           DoctorRepository doctorRepository,
                           PatientRepository patientRepository,
                           AppointmentRepository appointmentRepository,
                           PrescriptionRepository prescriptionRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public void run(String... args) {
        // Seed Users if none exist
        if (userRepository.count() == 0) {
            userRepository.save(new User("Hospital Admin", "admin@hospital.com", "admin123", "9876543200", "admin"));
            userRepository.save(new User("Dr. Amit Patel", "amit.patel@hospital.com", "doctor123", "9876543110", "doctor"));
            userRepository.save(new User("Dr. Rajesh Sharma", "rajesh.sharma@hospital.com", "doctor123", "9876543111", "doctor"));
            userRepository.save(new User("Rahul Verma", "rahul.verma@example.com", "patient123", "9876543210", "patient"));
        }

        // Seed Doctors if none exist
        if (doctorRepository.count() == 0) {
            doctorRepository.save(new Doctor("DOC-101", "Dr. Amit Patel", "General Medicine", "amit.patel@hospital.com", "9876543110", "8 Years"));
            doctorRepository.save(new Doctor("DOC-102", "Dr. Rajesh Sharma", "Cardiology", "rajesh.sharma@hospital.com", "9876543111", "12 Years"));
            doctorRepository.save(new Doctor("DOC-103", "Dr. Sneha Verma", "Dermatology", "sneha.verma@hospital.com", "9876543112", "6 Years"));
            doctorRepository.save(new Doctor("DOC-104", "Dr. Kavita Rao", "Pediatrics", "kavita.rao@hospital.com", "9876543113", "9 Years"));
            doctorRepository.save(new Doctor("DOC-105", "Dr. Manoj Kumar", "Orthopedics", "manoj.kumar@hospital.com", "9876543114", "11 Years"));
        }

        // Seed Patients if none exist
        if (patientRepository.count() == 0) {
            patientRepository.save(new Patient("PAT-001", "Rahul Verma", "rahul.verma@example.com", "9876543210", 24, "Male", "O+", "H.No 12/4, MG Road, Hyderabad", LocalDate.of(2026, 8, 10)));
            patientRepository.save(new Patient("PAT-002", "Priya Sharma", "priya.sharma@example.com", "9812345678", 32, "Female", "A+", "B-402, Green Park, Bangalore", LocalDate.of(2026, 8, 14)));
            patientRepository.save(new Patient("PAT-003", "Kiran Reddy", "kiran.reddy@example.com", "9123456780", 45, "Male", "B+", "Plot 77, Jubilee Hills, Hyderabad", LocalDate.of(2026, 9, 1)));
            patientRepository.save(new Patient("PAT-004", "Anjali Gupta", "anjali.gupta@example.com", "9988776655", 29, "Female", "AB+", "Flat 101, Lake View, Pune", LocalDate.of(2026, 9, 18)));
            patientRepository.save(new Patient("PAT-005", "Vikram Malhotra", "vikram.m@example.com", "9871234567", 52, "Male", "O-", "Sector 14, Gurgaon", LocalDate.of(2026, 10, 2)));
        }

        // Seed Appointments if none exist
        if (appointmentRepository.count() == 0) {
            appointmentRepository.save(new Appointment("APT-101", "Rahul Verma", "rahul.verma@example.com", "Dr. Rajesh Sharma", "Cardiology", "2026-10-15", "10:30 AM", "Chest tightness during exercise", "Confirmed"));
            appointmentRepository.save(new Appointment("APT-102", "Priya Sharma", "priya.sharma@example.com", "Dr. Sneha Verma", "Dermatology", "2026-10-20", "02:00 PM", "Skin rash and itching", "Pending"));
            appointmentRepository.save(new Appointment("APT-098", "Kiran Reddy", "kiran.reddy@example.com", "Dr. Amit Patel", "General Medicine", "2026-09-28", "11:00 AM", "Seasonal flu and body ache", "Completed"));
            appointmentRepository.save(new Appointment("APT-105", "Anjali Gupta", "anjali.gupta@example.com", "Dr. Kavita Rao", "Pediatrics", "2026-10-22", "03:00 PM", "Fever follow-up", "Pending"));
            appointmentRepository.save(new Appointment("APT-106", "Vikram Malhotra", "vikram.m@example.com", "Dr. Manoj Kumar", "Orthopedics", "2026-10-25", "09:30 AM", "Joint stiffness in knees", "Confirmed"));
        }

        // Seed Prescriptions if none exist
        if (prescriptionRepository.count() == 0) {
            prescriptionRepository.save(new Prescription("RX-501", "Rahul Verma", "Dr. Rajesh Sharma", "Mild Hypertension", "Amlodipine 5mg (Once daily)", "30 Days", "Check BP weekly and avoid excess salt", "2026-10-15"));
            prescriptionRepository.save(new Prescription("RX-502", "Priya Sharma", "Dr. Sneha Verma", "Common Cold & Allergy", "Paracetamol 650mg + Cetirizine 10mg", "5 Days", "Take with plenty of warm water", "2026-10-12"));
        }
    }
}
