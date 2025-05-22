package org.asturias.Infrastructure.Mappers.Response;

import org.asturias.Domain.DTO.Response.AppointmentDTO;
import org.asturias.Domain.DTO.Response.CalendarAppointmentDTO;
import org.asturias.Domain.DTO.Response.UserDTO;
import org.asturias.Domain.Enums.StatusAppointment;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Users;
import org.asturias.Infrastructure.Entities.AppointmentsEntity;
import org.asturias.Infrastructure.Entities.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface CalendarAppointmentMapper {

    // Mapear un modelo Appointments a AppointmentDTO
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "dateAppointment", source = "dateAppointment"),
            @Mapping(target = "details", source = "details"),
            @Mapping(target = "student", source = "student"), // Mapear Users a UserDTO
            @Mapping(target = "typeOfAppointmentName", ignore = true) // Puedes agregarlo después si necesitas el nombre
    })
    AppointmentDTO toAppointmentDTO(Appointments appointment);

    // Mapear un modelo Users a UserDTO
    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phone", source = "phone"),
            @Mapping(target = "numberDocument", source = "numberDocument"),
            @Mapping(target = "nameProgram", ignore = true) // Agregar después si necesitas el nombre del programa
    })
    UserDTO toUserDTO(Users user);

    // Mapear lista de modelos Appointments
    List<AppointmentDTO> toAppointmentDTOList(List<Appointments> appointments);

    // Mapear lista de modelos Users
    List<UserDTO> toUserDTOList(List<Users> users);

    // Mapear el objeto CalendarAppointmentDTO completo
    @Mappings({
            @Mapping(target = "totalAppointments", source = "totalAppointments"),
            @Mapping(target = "statuses", source = "statuses"),
            @Mapping(target = "appointments", source = "appointments")
    })
    CalendarAppointmentDTO toCalendarAppointmentDTO(Integer totalAppointments,
                                                    Map<StatusAppointment, Long> statuses,
                                                    List<AppointmentDTO> appointments);
}