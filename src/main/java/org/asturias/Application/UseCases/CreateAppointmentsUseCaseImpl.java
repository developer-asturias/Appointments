package org.asturias.Application.UseCases;

import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Students;
import org.asturias.Domain.Models.Users;
import org.asturias.Domain.Ports.In.CreateEntityUseCase;
import org.asturias.Domain.Ports.Out.AppointmentsRepositoryPort;
import org.asturias.Domain.Ports.Out.StudentsRepositoryPort;
import org.asturias.Domain.Ports.Out.UsersRepositoryPort;
import org.asturias.Infrastructure.Mappers.Request.AppointmentFormDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateAppointmentsUseCaseImpl  implements CreateEntityUseCase {


    private final AppointmentsRepositoryPort appointmentsRepositoryPort;
    private final UsersRepositoryPort usersRepositoryPort;
    private final StudentsRepositoryPort   studentsRepositoryPort;

    @Autowired
    private AppointmentFormDtoMapper appointmentFormDtoMapper;

    public CreateAppointmentsUseCaseImpl(AppointmentsRepositoryPort appointmentsRepositoryPort, UsersRepositoryPort usersRepositoryPort, StudentsRepositoryPort studentsRepositoryPort) {
        this.appointmentsRepositoryPort = appointmentsRepositoryPort;
        this.usersRepositoryPort = usersRepositoryPort;
        this.studentsRepositoryPort = studentsRepositoryPort;
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

}
