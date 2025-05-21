package org.asturias.Infrastructure.Config;


import org.asturias.Application.Services.AppointmentsService;
import org.asturias.Application.UseCases.CreateAppointmentsUseCaseImpl;
import org.asturias.Application.UseCases.RetrieveAppointmentsUseCaseImpl;
import org.asturias.Domain.Ports.Out.AppointmentsRepositoryPort;
import org.asturias.Domain.Ports.Out.UsersRepositoryPort;
import org.asturias.Infrastructure.Adapters.JpaAppointmentRepositoryAdapter;
import org.asturias.Infrastructure.Adapters.JpaUsersRepositoryAdapter;
import org.asturias.Infrastructure.Repositories.JpaUsersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    private final JpaUsersRepository userRepository;

    public ApplicationConfig(JpaUsersRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Bean
    public AppointmentsService appointmentsService( AppointmentsRepositoryPort appointmentsRepositoryPort,
                                                  UsersRepositoryPort usersRepositoryPort
    ) {
        return new AppointmentsService(
                new CreateAppointmentsUseCaseImpl(appointmentsRepositoryPort, usersRepositoryPort ),
                new RetrieveAppointmentsUseCaseImpl(appointmentsRepositoryPort, usersRepositoryPort)
        );
    }

    @Bean
    public AppointmentsRepositoryPort appointmentsRepositoryPort(JpaAppointmentRepositoryAdapter jpaAppointmentRepository) {
        return jpaAppointmentRepository;
    }


    @Bean
    public UsersRepositoryPort usersRepositoryPort(JpaUsersRepositoryAdapter jpaUserRepository) {
        return jpaUserRepository;
    }


}
