package org.asturias.Domain.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.asturias.Domain.Enums.StatusAppointment;

import java.time.LocalDateTime;



@Setter
@Getter
public class Appointments {
    
    private Long id;

    private LocalDateTime dateAppointment;

    private Long studentId;

    private Long programId;

    private StatusAppointment status;

    private Long typeOfAppointmentId;
    
    private String details;

    private Students student;

    @JsonIgnore
    private TypeOfAppointment typeOfAppointment;


    public Appointments(Long id, LocalDateTime dateAppointment, Long studentId, Long programId, StatusAppointment status, Long typeOfAppointmentId, String details, Students student, TypeOfAppointment typeOfAppointment) {
        this.id = id;
        this.dateAppointment = dateAppointment;
        this.studentId = studentId;
        this.programId = programId;
        this.status = status;
        this.typeOfAppointmentId = typeOfAppointmentId;
        this.details = details;
        this.student = student;
        this.typeOfAppointment = typeOfAppointment;
    }

    public Appointments() {

    }



}
