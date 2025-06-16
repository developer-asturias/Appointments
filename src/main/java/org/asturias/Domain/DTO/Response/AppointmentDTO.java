package org.asturias.Domain.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {

    private Long id;

    private LocalDateTime dateAppointment;

    private String typeOfAppointmentName;

    private String details;

    private UserDTO students;




}
