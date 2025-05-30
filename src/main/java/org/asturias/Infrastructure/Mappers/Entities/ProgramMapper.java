package org.asturias.Infrastructure.Mappers.Entities;

import org.asturias.Domain.Models.Program;
import org.asturias.Domain.Models.Role;
import org.asturias.Infrastructure.Entities.ProgramEntity;
import org.asturias.Infrastructure.Entities.RoleEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProgramMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
    })
    Program PROGRAM(ProgramEntity program);

    List<Program> PROGRAM_LIST(List<ProgramEntity> programEntities);

    @InheritInverseConfiguration
    ProgramEntity PROGRAM_ENTITY(Program program);

}
