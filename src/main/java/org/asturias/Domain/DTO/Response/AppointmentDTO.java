package org.asturias.Domain.DTO.Response;


import java.time.LocalDateTime;
import java.util.List;


public class AppointmentDTO {

    private Long id;

    private String name;

    private LocalDateTime dateAppointment;

    private String typeOfAppointmentName;

    private String details;

    private UserDTO student;

    public AppointmentDTO(Long id, String name, LocalDateTime dateAppointment, String typeOfAppointmentName, String details, UserDTO student) {
        this.id = id;
        this.name = name;
        this.dateAppointment = dateAppointment;
        this.typeOfAppointmentName = typeOfAppointmentName;
        this.details = details;
        this.student = student;
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

    public String getTypeOfAppointmentName() {
        return typeOfAppointmentName;
    }

    public void setTypeOfAppointmentName(String typeOfAppointmentName) {
        this.typeOfAppointmentName = typeOfAppointmentName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public UserDTO getStudent() {
        return student;
    }

    public void setStudent(UserDTO student) {
        this.student = student;
    }
}
