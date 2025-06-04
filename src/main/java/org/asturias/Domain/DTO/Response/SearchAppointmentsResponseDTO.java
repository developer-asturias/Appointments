package org.asturias.Domain.DTO.Response;

import lombok.Getter;
import lombok.Setter;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Domain.Models.TypeOfAppointment;

import java.time.LocalDateTime;


@Getter
@Setter
public class SearchAppointmentsResponseDTO {

    private Long idAppointment;
    private String details;
    private  String typeOfAppointmentName;
    private StatusAppointment status;
    private LocalDateTime date;

    public SearchAppointmentsResponseDTO(Long idAppointment, String details, String typeOfAppointmentName, StatusAppointment status, LocalDateTime date) {
        this.idAppointment = idAppointment;
        this.details = details;
        this.typeOfAppointmentName = typeOfAppointmentName;
        this.status = status;
        this.date = date;
    }
}
