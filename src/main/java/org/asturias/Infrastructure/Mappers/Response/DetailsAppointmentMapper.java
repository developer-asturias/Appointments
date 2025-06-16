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
//            @Mapping(target = "appointmentId", source = "appointments.id"),
//            @Mapping(target = "typeAppointmentName", source = "appointments.typeOfAppointment.name"),
//            @Mapping(target = "appointmentDetails", source = "appointments.details"),
//            @Mapping(target = "appointmentDate", source = "appointments.dateAppointment"),
//            @Mapping(target = "numberDocument", source = "appointments.student.numberDocument"),
//            @Mapping(target = "nameStudent", source = "appointments.student.name"),
            @Mapping(target = "phone", source = "appointments.students.phone"),
//            @Mapping(target = "email", source = "appointments.student.email"),
//            @Mapping(target = "programName", source = "appointments.student.program.name" ),
//            @Mapping(target = "mentorName", source = "appointments.user.name" )
    })
    DetailsAppointmentDTO toDetailsAppointmentDTO(Appointments appointments);
}

