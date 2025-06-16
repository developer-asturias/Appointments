package org.asturias.Infrastructure.Mappers.Entities;

import org.asturias.Domain.Models.Students;
import org.asturias.Infrastructure.Entities.StudentsEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import java.util.List;


@Mapper(componentModel = "spring")
public interface StudentsMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "createAt", target = "createAt"),
            @Mapping(source = "updateAt", target = "updateAt"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "numberDocument", target = "numberDocument"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "program", target = "program"),

    })
    Students STUDENTS(StudentsEntity students);

    List<Students> STUDENTS_LIST(List<StudentsEntity> studentsEntities);

    @InheritInverseConfiguration
    StudentsEntity  STUDENTS_ENTITY(Students students);
}
