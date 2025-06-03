package org.asturias.Domain.DTO.Response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Setter
@Getter
public class AppointmentDTO {

    private Long id;

    private LocalDateTime dateAppointment;

    private String typeOfAppointmentName;

    private String details;

    private UserDTO student;

    public AppointmentDTO(Long id, LocalDateTime dateAppointment, String typeOfAppointmentName, String details, UserDTO student) {
        this.id = id;
        this.dateAppointment = dateAppointment;
        this.typeOfAppointmentName = typeOfAppointmentName;
        this.details = details;
        this.student = student;
    }



}
