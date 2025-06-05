package org.asturias.Infrastructure.Mappers.Response;


import org.asturias.Domain.DTO.Response.AppointmentsPageableResponseDTO;
import org.asturias.Domain.DTO.Response.SearchAppointmentsResponseDTO;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Students;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface getAllAppointmentsPageableResponseMapper {

    @Mapping(target = "userName", source = "appointment.id")
    @Mapping(target = "Email", source = "appointment.details")
    @Mapping(target = "program", source = "appointment.typeOfAppointment.name")
    @Mapping(target = "typeOfAppointmentName", source = "appointment.status")
    @Mapping(target = "date", source = "appointment.dateAppointment")
    @Mapping(target = "status", source = "appointment.dateAppointment")
    @Mapping(target = "mentorName", source = "appointment.dateAppointment")
    AppointmentsPageableResponseDTO toDto(Appointments appointment, Students student);
}
