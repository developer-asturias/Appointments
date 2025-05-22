package org.asturias.Domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.asturias.Domain.Enums.StatusAppointment;



@Getter
@Setter
@AllArgsConstructor
public class DetailsAppointmentDTO {


    private String appointmentName;

    private String appointmentDescription;

    private String appointmentDate;

    private String programName;

    private String typeAppointment;

    private StatusAppointment appointmentStatus;

    private String numberDocument;

    private String nameStudent;

    private String phone;

    private String email;


}
