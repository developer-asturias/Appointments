package org.asturias.Infrastructure.Mappers.Entities;

import org.asturias.Domain.Models.Program;
import org.asturias.Domain.Models.Schedule;
import org.asturias.Infrastructure.Entities.ProgramEntity;
import org.asturias.Infrastructure.Entities.ScheduleEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "isActive", target = "isActive"),
            @Mapping(source = "dayOfWeek", target = "dayOfWeek"),
            @Mapping(source = "startTime", target = "startTime"),
            @Mapping(source = "endTime", target = "endTime"),
            @Mapping(source = "typeOfAppointmentId", target = "typeOfAppointmentId"),
            @Mapping(source = "typeOfAppointment", target = "typeOfAppointment"),
            @Mapping(source = "capacity", target = "capacity")

    })
    Schedule SCHEDULE(ScheduleEntity schedule);

    List<Schedule> SCHEDULE_LIST(List<ScheduleEntity> scheduleEntities);

    @InheritInverseConfiguration
    ScheduleEntity SCHEDULE_ENTITY(Schedule schedule);
}
