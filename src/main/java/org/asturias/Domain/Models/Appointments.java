package org.asturias.Domain.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.asturias.Domain.Enums.StatusAppointment;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Appointments {
    
    private Long id;

    private LocalDateTime dateAppointment;

    private Users user;

    private Long userId;

    private Long studentId;

    private StatusAppointment status;

    private Long typeOfAppointmentId;
    
    private String details;

    private Students student;

    @JsonIgnore
    private TypeOfAppointment typeOfAppointment;


}
