package org.asturias.Application.UseCases;

import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Schedule;
import org.asturias.Domain.Models.Users;
import org.asturias.Domain.Ports.In.UpdateEntityUseCase;
import org.asturias.Domain.Ports.Out.AppointmentsRepositoryPort;
import org.asturias.Domain.Ports.Out.ScheduleRepositoryPort;
import org.asturias.Domain.Ports.Out.UsersRepositoryPort;

import java.util.Optional;

public class UpdateAppointmentUseCaseImpl implements UpdateEntityUseCase {

    private final AppointmentsRepositoryPort appointmentsRepositoryPort;
    private final UsersRepositoryPort usersRepositoryPort;
    private final ScheduleRepositoryPort scheduleRepositoryPort;


    public UpdateAppointmentUseCaseImpl(AppointmentsRepositoryPort appointmentsRepositoryPort, UsersRepositoryPort usersRepositoryPort, ScheduleRepositoryPort scheduleRepositoryPort) {
        this.appointmentsRepositoryPort = appointmentsRepositoryPort;
        this.usersRepositoryPort = usersRepositoryPort;
        this.scheduleRepositoryPort = scheduleRepositoryPort;
    }




    @Override
    public Optional<Users> updateUsers(Long id, Users users) {
        return usersRepositoryPort.update(users);
    }

    @Override
    public Optional<Schedule> updateSchedule(Long id, Schedule schedule) {
        return scheduleRepositoryPort.update(id, schedule);
    }

    @Override
    public Optional<Appointments> updateAppointment(Long id, Appointments schedule) {
        return appointmentsRepositoryPort.update(schedule);
    }

    @Override
    public Optional<Appointments> updateAppointmentMentor(Long id, Long mentorId) {
        return appointmentsRepositoryPort.updateAppointmentMentor(id, mentorId);
    }
}
