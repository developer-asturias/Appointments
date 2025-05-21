package org.asturias.Domain.Ports.In;

import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Users;

public interface CreateEntityUseCase {

    Appointments createAppointments (Appointments appointments);
    Users createUsers (Users users);


}
