package org.asturias.Infrastructure.Mappers.Request;


import org.asturias.Domain.DTO.Request.AppointmentFormDTO;
import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Students;
import org.asturias.Domain.Models.Users;
import org.asturias.Infrastructure.Entities.AppointmentsEntity;
import org.asturias.Infrastructure.Entities.UsersEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AppointmentFormDtoMapper {



    // Mapear el DTO hacia el modelo Appointments
    @Mappings({
                @Mapping(source = "date", target = "dateAppointment"),
                @Mapping(source = "userId", target = "studentId"),
                @Mapping(source = "typeOfAppointmentId", target = "typeOfAppointmentId"),
                @Mapping(source = "details", target = "details")
        })
    Appointments mapToAppointments(AppointmentFormDTO appointmentFormDTO);

        // Mapear el DTO hacia el modelo Users
        @Mappings({
                @Mapping(source = "userName", target = "name"),
                @Mapping(source = "userEmail", target = "email"),
                @Mapping(source = "phone", target = "phone"),
                @Mapping(source = "programId", target = "programId"),
                @Mapping(source = "numberDocument", target = "numberDocument")
        })
        Students mapToUsers(AppointmentFormDTO appointmentFormDTO);

    }

