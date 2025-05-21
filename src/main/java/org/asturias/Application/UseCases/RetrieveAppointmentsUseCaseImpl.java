package org.asturias.Application.UseCases;

import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Users;
import org.asturias.Domain.Ports.In.RetrieveEntityUseCase;
import org.asturias.Domain.Ports.Out.AppointmentsRepositoryPort;
import org.asturias.Domain.Ports.Out.UsersRepositoryPort;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RetrieveAppointmentsUseCaseImpl implements RetrieveEntityUseCase {

    private final AppointmentsRepositoryPort appointmentsRepositoryPort;
    private final UsersRepositoryPort usersRepositoryPort;

    public RetrieveAppointmentsUseCaseImpl(AppointmentsRepositoryPort appointmentsRepositoryPort, UsersRepositoryPort usersRepositoryPort) {
        this.appointmentsRepositoryPort = appointmentsRepositoryPort;
        this.usersRepositoryPort = usersRepositoryPort;
    }


    @Override
    public Optional<Users> getUserById(Long id) {
        return usersRepositoryPort.findById(id);
    }

    @Override
    public Optional<Appointments> findAppointmentById(Long id) {
        return  appointmentsRepositoryPort.findById(id);
    }


    @Override
    public List<Appointments> findByDateAppointmentBetween(LocalDateTime start, LocalDateTime end) {
        return appointmentsRepositoryPort.findByDateAppointmentBetween(start, end);
    }
}
