package org.asturias.Domain.DTO.Response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseAppointmentDTO {

    private LocalDateTime date;
    private String nameStudent;
    private String userEmail;

    public ResponseAppointmentDTO(LocalDateTime date, String nameStudent) {
        this.date = date;
        this.nameStudent = nameStudent;
    }

    public ResponseAppointmentDTO() {

    }
}
