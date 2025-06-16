package org.asturias.Infrastructure.Mappers.Response;

import org.asturias.Domain.DTO.Response.SearchAppointmentsResponseDTO;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Students;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SearchAppointmentsMapper {

        @Mapping(target = "idAppointment", source = "appointment.id")
        @Mapping(target = "details", source = "appointment.details")
//        @Mapping(target = "typeOfAppointmentName", source = "appointment.typeOfAppointment.name")
//        @Mapping(target = "date", source = "appointment.dateAppointment")
        SearchAppointmentsResponseDTO toDto(Appointments appointment, Students student);
}