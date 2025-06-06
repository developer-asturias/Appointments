package org.asturias.Domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;




@Setter
@Getter
@AllArgsConstructor
public class DetailsAppointmentDTO {

    private Long appointmentId;

    private String typeAppointmentName;

    private String appointmentDetails;

    private String appointmentDate;

    private String programName;

    private String numberDocument;

    private String nameStudent;

    private String phone;

    private String email;

    private String mentorName;


}
