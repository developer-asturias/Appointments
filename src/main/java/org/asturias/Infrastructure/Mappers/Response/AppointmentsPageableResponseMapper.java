package org.asturias.Infrastructure.Mappers.Response;


import org.asturias.Domain.DTO.Response.AppointmentsPageableResponseDTO;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Students;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentsPageableResponseMapper {

    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "userName", source = "appointment.student.name")
    @Mapping(target = "Email", source = "appointment.student.email")
    @Mapping(target = "program", source = "appointment.student.program.name")
    @Mapping(target = "typeOfAppointmentName", source = "appointment.typeOfAppointment.name")
    @Mapping(target = "date", source = "appointment.dateAppointment")
    @Mapping(target = "status", source = "appointment.status")
    @Mapping(target = "mentorName", source = "appointment.user.name")
    AppointmentsPageableResponseDTO toDto(Appointments appointment);
}
