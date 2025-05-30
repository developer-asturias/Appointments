package org.asturias.Domain.Ports.In;

import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Schedule;
import org.asturias.Domain.Models.Users;

import java.util.Optional;

public interface UpdateEntityUseCase {

    Optional<Users> updateUsers(Long id, Users users);

    Optional<Schedule> updateSchedule (Long id, Schedule schedule);

    Optional<Appointments> updateAppointment(Long id, Appointments appointments);


}
