package org.asturias.Infrastructure.Mappers.Entities;

import org.asturias.Domain.Models.TypeOfAppointment;
import org.asturias.Domain.Models.Users;
import org.asturias.Infrastructure.Entities.TypeOfAppointmentEntity;
import org.asturias.Infrastructure.Entities.UsersEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;


@Mapper(componentModel = "spring")
public interface TypeAppointmentMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "schedules", target = "schedules"),

    })
    TypeOfAppointment TYPE_OF_APPOINTMENT(TypeOfAppointmentEntity typeOfAppointment);

    List<TypeOfAppointment> TYPE_OF_APPOINTMENTS(List<TypeOfAppointmentEntity> ofAppointmentEntities);

    @InheritInverseConfiguration
    TypeOfAppointmentEntity TYPE_OF_APPOINTMENT_ENTITY(TypeOfAppointment users);
}
