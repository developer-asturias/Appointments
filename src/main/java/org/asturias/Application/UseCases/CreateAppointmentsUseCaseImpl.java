package org.asturias.Application.UseCases;

import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.Exceptions.ScheduleConflictException;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Schedule;
import org.asturias.Domain.Models.Students;
import org.asturias.Domain.Models.Users;
import org.asturias.Domain.Ports.In.CreateEntityUseCase;
import org.asturias.Domain.Ports.Out.AppointmentsRepositoryPort;
import org.asturias.Domain.Ports.Out.ScheduleRepositoryPort;
import org.asturias.Domain.Ports.Out.StudentsRepositoryPort;
import org.asturias.Domain.Ports.Out.UsersRepositoryPort;
import org.asturias.Infrastructure.Mappers.Request.AppointmentFormDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CreateAppointmentsUseCaseImpl  implements CreateEntityUseCase {


    private final AppointmentsRepositoryPort appointmentsRepositoryPort;
    private final UsersRepositoryPort usersRepositoryPort;
    private final StudentsRepositoryPort   studentsRepositoryPort;
    private final ScheduleRepositoryPort scheduleRepositoryPort;

    @Autowired
    private AppointmentFormDtoMapper appointmentFormDtoMapper;

    public CreateAppointmentsUseCaseImpl(AppointmentsRepositoryPort appointmentsRepositoryPort, UsersRepositoryPort usersRepositoryPort, StudentsRepositoryPort studentsRepositoryPort, ScheduleRepositoryPort scheduleRepositoryPort) {
        this.appointmentsRepositoryPort = appointmentsRepositoryPort;
        this.usersRepositoryPort = usersRepositoryPort;
        this.studentsRepositoryPort = studentsRepositoryPort;
        this.scheduleRepositoryPort = scheduleRepositoryPort;
    }

    @Override
    public Appointments createAppointments(Appointments appointments) {
        return appointmentsRepositoryPort.save(appointments);
    }

    @Override
    public Users createUsers(Users users) {
        return usersRepositoryPort.save(users);
    }

    @Override
    public Students createStudents(Students students) {
        return studentsRepositoryPort.save(students);
    }




    @Override
    public Schedule createSchedule(Schedule newSchedule) {
        List<Schedule> existingSchedules = scheduleRepositoryPort
                .findByDayAndType(newSchedule.getDayOfWeek(), newSchedule.getTypeOfAppointmentId());

        existingSchedules.forEach(existing -> {
            if (newSchedule.isDuplicateOf(existing)) {
                throw new ScheduleConflictException("Ya existe un horario idéntico para este tipo de cita");
            }
            if (newSchedule.conflictsWith(existing)) {
                throw new ScheduleConflictException("El horario se traslapa con otro existente");
            }
        });

        return scheduleRepositoryPort.save(newSchedule);
    }


}
