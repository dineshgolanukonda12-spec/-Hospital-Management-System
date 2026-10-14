package com.hospital.backend.service;

import com.hospital.backend.entity.Doctor;
import com.hospital.backend.entity.Patient;
import com.hospital.backend.entity.User;
import com.hospital.backend.repository.DoctorRepository;
import com.hospital.backend.repository.PatientRepository;
import com.hospital.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AuthService(UserRepository userRepository,
                       PatientRepository patientRepository,
                       DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public User register(User userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("User with email " + userRequest.getEmail() + " already exists.");
        }

        String role = userRequest.getRole() != null ? userRequest.getRole().toLowerCase() : "patient";
        userRequest.setRole(role);
        User savedUser = userRepository.save(userRequest);

        // Synchronize corresponding profile record
        if ("patient".equalsIgnoreCase(role)) {
            String patId = "PAT-" + String.format("%03d", savedUser.getId());
            Patient patient = new Patient(
                    patId,
                    savedUser.getFullName(),
                    savedUser.getEmail(),
                    savedUser.getPhone(),
                    25,
                    "Not specified",
                    "O+",
                    "Default Address",
                    LocalDate.now()
            );
            patientRepository.save(patient);
        } else if ("doctor".equalsIgnoreCase(role)) {
            String docId = "DOC-" + String.format("%03d", savedUser.getId());
            Doctor doctor = new Doctor(
                    docId,
                    savedUser.getFullName().startsWith("Dr.") ? savedUser.getFullName() : "Dr. " + savedUser.getFullName(),
                    "General Medicine",
                    savedUser.getEmail(),
                    savedUser.getPhone(),
                    "1 Year"
            );
            doctorRepository.save(doctor);
        }

        return savedUser;
    }

    public User login(String email, String password, String role) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        User user = optionalUser.get();
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        if (role != null && !role.trim().isEmpty() && !user.getRole().equalsIgnoreCase(role.trim())) {
            throw new IllegalArgumentException("User does not have role: " + role);
        }

        return user;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
