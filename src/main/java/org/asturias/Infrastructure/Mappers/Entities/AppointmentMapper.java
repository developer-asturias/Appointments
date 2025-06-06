package org.asturias.Infrastructure.Mappers.Entities;


import org.asturias.Domain.Models.Appointments;
import org.asturias.Infrastructure.Entities.AppointmentsEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentsMapper.class})
public interface AppointmentMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "dateAppointment", target = "dateAppointment"),
            @Mapping(source = "studentId", target = "studentId"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "typeOfAppointmentId", target = "typeOfAppointmentId"),
            @Mapping(source = "details", target = "details"),
            @Mapping(source = "students", target = "student"),
    })


    Appointments APPOINTMENTS(AppointmentsEntity appointmentsEntity);

    List<Appointments> APPOINTMENTS_LIST(List<AppointmentsEntity> appointmentsEntities);

    @InheritInverseConfiguration
    @Mappings({
            @Mapping(source = "studentId", target = "studentId"),
            @Mapping(source = "student", target = "students")
    })
    AppointmentsEntity APPOINTMENTS_ENTITY(Appointments appointments);
}
