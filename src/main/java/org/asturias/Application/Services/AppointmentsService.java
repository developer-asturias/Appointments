package org.asturias.Application.Services;

import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Users;
import org.asturias.Domain.Ports.In.CreateAppointmentAndUser;
import org.asturias.Domain.Ports.In.CreateEntityUseCase;
import org.asturias.Domain.Ports.In.RetrieveEntityUseCase;
import org.asturias.Infrastructure.Mappers.Request.AppointmentFormDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AppointmentsService  implements CreateEntityUseCase, RetrieveEntityUseCase, CreateAppointmentAndUser {


    private final CreateEntityUseCase createEntityUseCase;
    private final RetrieveEntityUseCase retrieveEntitiesUseCase;

    @Autowired
    private AppointmentFormDtoMapper appointmentFormDtoMapper;

    public AppointmentsService(CreateEntityUseCase createEntityUseCase, RetrieveEntityUseCase retrieveEntitiesUseCase) {
        this.createEntityUseCase = createEntityUseCase;
        this.retrieveEntitiesUseCase = retrieveEntitiesUseCase;
    }


    @Override
    public Appointments createAppointments(Appointments appointments) {
        return createEntityUseCase.createAppointments(appointments);
    }

    @Override
    public Users createUsers(Users users) {
        return createEntityUseCase.createUsers(users);
    }


    @Override
    public Optional<Users> getUserById(Long id) {
        return retrieveEntitiesUseCase.getUserById(id);
    }

    @Override
    public Optional<Appointments> findAppointmentById(Long id) {
        return retrieveEntitiesUseCase.findAppointmentById(id);
    }

    @Override
    public List<Appointments> findByDateAppointmentBetween(LocalDateTime start, LocalDateTime end) {
        return retrieveEntitiesUseCase.findByDateAppointmentBetween(start, end);
    }

    @Override
    public void createAppointmentAndUser(AppointmentFormDTO formDTO) {
        // 1. Convertir el DTO en un objeto de dominio Users
        Users user = appointmentFormDtoMapper.mapToUsers(formDTO);
        // 2. Persistir al usuario para obtener el usuario guardado con ID asignado
        Users savedUser = createUsers(user);
        // 3. Convertir el DTO en un objeto de dominio Appointments
        Appointments appointment = appointmentFormDtoMapper.mapToAppointments(formDTO);
        // 4. Asignar el ID del usuario al objeto de dominio Appointments
        appointment.setStudentId(savedUser.getId());
        // 5. Persistir la cita en la base de datos
        createAppointments(appointment);
    }
}