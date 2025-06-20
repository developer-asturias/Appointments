package org.asturias.Infrastructure.Mappers.Response;


import org.asturias.Domain.DTO.Response.ScheduleResponseDTO;
import org.asturias.Domain.Models.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleResponseMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "dayOfWeek", source = "dayOfWeek")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    @Mapping(target = "typeOfAppointmentId", source = "typeOfAppointmentId")
    @Mapping(target = "typeOfAppointmentName", source = "schedule.typeOfAppointment.name")
    @Mapping(target = "capacity", source = "capacity")

    ScheduleResponseDTO SCHEDULE_RESPONSE_DTO(Schedule schedule);


}
