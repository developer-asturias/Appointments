package org.asturias.Infrastructure.Mappers.Entities;


import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Users;
import org.asturias.Infrastructure.Entities.AppointmentsEntity;
import org.asturias.Infrastructure.Entities.UsersEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "dateAppointment", target = "dateAppointment"),
            @Mapping(source = "userId", target = "studentId"),
//            @Mapping(source = "programId", target = "programId"),
//            @Mapping(source = "teamsLink", target = "teamsLink"),
            @Mapping(source = "typeOfAppointmentId", target = "typeOfAppointmentId"),
            @Mapping(source = "details", target = "details"),
    })
    Appointments APPOINTMENTS(AppointmentsEntity appointments);

    List<Appointments> APPOINTMENTS_LIST(List<AppointmentsEntity> appointmentsEntities);

    @InheritInverseConfiguration
    AppointmentsEntity  APPOINTMENTS_ENTITY(Appointments appointments);

}
