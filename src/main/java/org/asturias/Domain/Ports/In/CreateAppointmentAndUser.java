package org.asturias.Domain.Ports.In;

import org.asturias.Domain.DTO.Request.AppointmentFormDTO;

public interface CreateAppointmentAndUser {

    void createAppointmentAndUser(AppointmentFormDTO formDTO);
}
