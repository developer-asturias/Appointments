package org.asturias.Domain.Ports.In;

import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.DTO.Response.CalendarAppointmentDTO;

public interface CreateAppointmentAndUser {

    void createAppointmentAndUser(AppointmentFormDTO formDTO);
    CalendarAppointmentDTO getCalendarAppointmentDTO(String start, String end);
}
