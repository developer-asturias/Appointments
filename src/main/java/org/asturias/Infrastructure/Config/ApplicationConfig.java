package org.asturias.Infrastructure.Config;


import org.asturias.Application.Services.AppointmentsService;
import org.asturias.Application.UseCases.CreateAppointmentsUseCaseImpl;
import org.asturias.Application.UseCases.RetrieveAppointmentsUseCaseImpl;
import org.asturias.Domain.Ports.Out.*;
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
                                                  UsersRepositoryPort usersRepositoryPort,
                                                    ProgramRepositoryPort programRepositoryPort,
                                                    TypeOfAppointmentRepositoryPort typeOfAppointmentRepositoryPort,
                                                    StudentsRepositoryPort studentsRepositoryPort,
                                                    ScheduleRepositoryPort scheduleRepositoryPort
    ) {
        return new AppointmentsService(
                new CreateAppointmentsUseCaseImpl(appointmentsRepositoryPort, usersRepositoryPort,studentsRepositoryPort ),
                new RetrieveAppointmentsUseCaseImpl(appointmentsRepositoryPort, usersRepositoryPort, programRepositoryPort, typeOfAppointmentRepositoryPort, studentsRepositoryPort, scheduleRepositoryPort)
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
