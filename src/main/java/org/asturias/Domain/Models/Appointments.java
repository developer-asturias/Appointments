package org.asturias.Domain.Models;


import org.asturias.Domain.Enums.StatusAppointment;

import java.time.LocalDateTime;



public class Appointments {
    
    private Long id;
    
    private String name;
    
    private LocalDateTime dateAppointment;

    private Long studentId;

    private Long programId;

    private String teamsLink;

    private StatusAppointment status;

    private Long typeOfAppointmentId;
    
    private String details;

    private Users student;


    public Appointments(Long id, String name, LocalDateTime dateAppointment, Long studentId, Long programId, String teamsLink, StatusAppointment status, Long typeOfAppointmentId, String details, Users student) {
        this.id = id;
        this.name = name;
        this.dateAppointment = dateAppointment;
        this.studentId = studentId;
        this.programId = programId;
        this.teamsLink = teamsLink;
        this.status = status;
        this.typeOfAppointmentId = typeOfAppointmentId;
        this.details = details;
        this.student = student;
    }

    public Appointments() {

    }


    public StatusAppointment getStatus() {
        return status;
    }

    public void setStatus(StatusAppointment status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateAppointment() {
        return dateAppointment;
    }

    public void setDateAppointment(LocalDateTime dateAppointment) {
        this.dateAppointment = dateAppointment;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getTeamsLink() {
        return teamsLink;
    }

    public void setTeamsLink(String teamsLink) {
        this.teamsLink = teamsLink;
    }

    public Long getTypeOfAppointmentId() {
        return typeOfAppointmentId;
    }

    public void setTypeOfAppointmentId(Long typeOfAppointmentId) {
        this.typeOfAppointmentId = typeOfAppointmentId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Users getStudent() {
        return student;
    }

    public void setStudent(Users student) {
        this.student = student;
    }
}
