package org.asturias.Domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Domain.Models.TypeOfAppointment;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchAppointmentsResponseDTO {

    private Long idAppointment;
    private String details;
    private  String typeOfAppointmentName;
//    private StatusAppointment status;
    private LocalDateTime date;


}
