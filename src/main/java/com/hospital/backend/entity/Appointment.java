package com.hospital.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appointmentId; // e.g. APT-101

    private String patient; // Patient full name

    private String patientEmail;

    private String doctor; // Doctor full name

    private String department;

    private String date; // YYYY-MM-DD

    private String time; // e.g. 10:00 AM

    @Column(length = 1000)
    private String symptoms;

    private String status = "Pending"; // Pending, Confirmed, Completed, Cancelled

    public Appointment() {
    }

    public Appointment(String appointmentId, String patient, String patientEmail, String doctor, String department, String date, String time, String symptoms, String status) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.patientEmail = patientEmail;
        this.doctor = doctor;
        this.department = department;
        this.date = date;
        this.time = time;
        this.symptoms = symptoms;
        this.status = status != null ? status : "Pending";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
