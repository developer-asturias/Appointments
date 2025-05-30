package org.asturias.Infrastructure.Mappers.Response;
import org.apache.catalina.User;
import org.asturias.Domain.DTO.Response.DetailsAppointmentDTO;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;



@Mapper(componentModel = "spring")
public interface DetailsAppointmentMapper {

    @Mappings({
            @Mapping(target = "appointmentName", source = "appointment.name"),
            @Mapping(target = "appointmentDescription", source = "appointment.details"),
            @Mapping(target = "appointmentDate", expression = "java(appointment.getDateAppointment().toString())"),
//            @Mapping(target = "programName", constant = ""), // Dejamos vacío por ahora
//            @Mapping(target = "typeAppointment", constant = ""), // Dejamos vacío por ahora
            @Mapping(target = "appointmentStatus", source = "appointment.status"),
            @Mapping(target = "numberDocument", source = "user.numberDocument"),
//            @Mapping(target = "linkTeams", source = "appointment.teamsLink"),
            @Mapping(target = "nameStudent", source = "user.name"),
            @Mapping(target = "phone", source = "user.phone"),
            @Mapping(target = "email", source = "user.email")
    })
    DetailsAppointmentDTO toDetailsAppointmentDTO(Appointments appointment, Users user);
}

