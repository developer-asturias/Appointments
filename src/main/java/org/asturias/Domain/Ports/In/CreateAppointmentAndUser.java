package org.asturias.Domain.Ports.In;

import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.DTO.Response.CalendarAppointmentDTO;
import org.asturias.Domain.DTO.Response.ResponseAppointmentDTO;
import org.springframework.http.ResponseEntity;

public interface CreateAppointmentAndUser {

    ResponseAppointmentDTO createAppointmentAndUser(AppointmentFormDTO formDTO);
    CalendarAppointmentDTO getCalendarAppointmentDTO(String start, String end);
}
