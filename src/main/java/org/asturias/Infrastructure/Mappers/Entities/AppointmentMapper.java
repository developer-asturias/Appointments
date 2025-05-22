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

@Mapper(componentModel = "spring", uses = {UsersMapper.class})
public interface AppointmentMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "dateAppointment", target = "dateAppointment"),
            @Mapping(source = "userId", target = "studentId"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "typeOfAppointmentId", target = "typeOfAppointmentId"),
            @Mapping(source = "details", target = "details"),
            @Mapping(source = "users", target = "student"),
            @Mapping(target = "programId", ignore = true),
            @Mapping(target = "teamsLink", ignore = true)
    })
    Appointments APPOINTMENTS(AppointmentsEntity appointmentsEntity);

//    List<Appointments> APPOINTMENTS(List<AppointmentsEntity> appointmentsEntities);

    @InheritInverseConfiguration
    @Mappings({
            @Mapping(source = "studentId", target = "userId"),
            @Mapping(source = "student", target = "users")
    })
    AppointmentsEntity APPOINTMENTS_ENTITY(Appointments appointments);
}
