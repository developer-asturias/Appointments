package org.asturias.Infrastructure.Mappers.Entities;


import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.dataTreatments;
import org.asturias.Infrastructure.Entities.AppointmentsEntity;
import org.asturias.Infrastructure.Entities.dataTreatmentsEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {StudentsMapper.class})
public interface dataTreatmentsMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "accepted", target = "accepted"),
            @Mapping(source = "studentId", target = "studentId"),
    })
    dataTreatments DATA_TREATMENTS(dataTreatmentsEntity dataTreatmentsEntity);
    @InheritInverseConfiguration
    @Mappings({
            @Mapping(source = "studentId", target = "studentId"),
    })
    dataTreatmentsEntity DATA_TREATMENTS_ENTITY(dataTreatments dataTreatments);
}
