package com.hospital.backend.service;

import com.hospital.backend.entity.Doctor;
import com.hospital.backend.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor addDoctor(Doctor doctor) {
        if (doctor.getDoctorId() == null || doctor.getDoctorId().trim().isEmpty()) {
            doctor.setDoctorId("DOC-" + (100 + (int) (Math.random() * 900)));
        }
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        return doctorRepository.findById(id).map(doc -> {
            doc.setName(doctorDetails.getName());
            doc.setDept(doctorDetails.getDept());
            doc.setEmail(doctorDetails.getEmail());
            doc.setPhone(doctorDetails.getPhone());
            doc.setExperience(doctorDetails.getExperience());
            return doctorRepository.save(doc);
        }).orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + id));
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    public void deleteByDoctorId(String doctorId) {
        doctorRepository.findByDoctorId(doctorId).ifPresent(doctorRepository::delete);
    }
}
