package org.asturias.Application.UseCases;

import org.asturias.Domain.Ports.In.UpdateEntityUseCase;
import org.asturias.Domain.Ports.Out.AppointmentsRepositoryPort;
import org.asturias.Domain.Ports.Out.UsersRepositoryPort;

public class UpdateAppointmentUseCaseImpl implements UpdateEntityUseCase {

    private final AppointmentsRepositoryPort appointmentsRepositoryPort;
    private final UsersRepositoryPort usersRepositoryPort;

    public UpdateAppointmentUseCaseImpl(AppointmentsRepositoryPort appointmentsRepositoryPort, UsersRepositoryPort usersRepositoryPort) {
        this.appointmentsRepositoryPort = appointmentsRepositoryPort;
        this.usersRepositoryPort = usersRepositoryPort;
    }
}
