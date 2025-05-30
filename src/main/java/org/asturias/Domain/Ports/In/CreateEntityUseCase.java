package org.asturias.Domain.Ports.In;

import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Schedule;
import org.asturias.Domain.Models.Students;
import org.asturias.Domain.Models.Users;

public interface CreateEntityUseCase {

    Appointments createAppointments (Appointments appointments);
    Users createUsers (Users users);
    Students createStudents  (Students students);
    Schedule createSchedule (Schedule schedule);


}
