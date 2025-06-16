package org.asturias.Domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.asturias.Domain.Enums.StatusAppointment;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentsPageableResponseDTO {


    private Long appointmentId;
    private String userName;

    private String Email;

    private String program;

    private String typeOfAppointmentName;

    private LocalDateTime date;

//    private StatusAppointment status;

    private String mentorName;

}
