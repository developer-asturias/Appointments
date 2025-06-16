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
            @Mapping(source = "details", target = "details"),
            @Mapping(source = "students", target = "students"),
            @Mapping(source = "schedule", target = "schedule"),
            @Mapping(source = "updateAt", target = "updateAt"),
            @Mapping(source = "createAt", target = "createAt"),
            @Mapping(source = "mentors", target = "mentors"),
            @Mapping(source = "mentorNotes", target = "mentorNotes"),
            @Mapping(source = "meetingUrl", target = "meetingUrl"),
            @Mapping(source = "startTime", target = "startTime"),
            @Mapping(source = "endTime", target = "endTime"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "mentorJoinedAt", target = "mentorJoinedAt"),
            @Mapping(source = "studentJoinedAt", target = "studentJoinedAt"),
    })


    Appointments APPOINTMENTS(AppointmentsEntity appointmentsEntity);

    List<Appointments> APPOINTMENTS_LIST(List<AppointmentsEntity> appointmentsEntities);

    @InheritInverseConfiguration
    @Mappings({
//            @Mapping(source = "studentId", target = "studentId"),
            @Mapping(source = "students", target = "students")
    })
    AppointmentsEntity APPOINTMENTS_ENTITY(Appointments appointments);
}
