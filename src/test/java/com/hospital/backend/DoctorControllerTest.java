package com.hospital.backend;

import com.hospital.backend.entity.Doctor;
import com.hospital.backend.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DoctorControllerTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void testDoctorPersistence() {
        Doctor doc = new Doctor("DOC-TEST", "Dr. Unit Test", "Cardiology", "test@test.com", "9999999999", "5 Years");
        Doctor saved = doctorRepository.save(doc);
        assertNotNull(saved.getId());

        List<Doctor> doctors = doctorRepository.findAll();
        assertFalse(doctors.isEmpty());

        doctorRepository.delete(saved);
    }
}
