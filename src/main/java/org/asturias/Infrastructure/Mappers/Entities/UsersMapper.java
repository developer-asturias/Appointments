package org.asturias.Infrastructure.Mappers.Entities;


import org.asturias.Domain.Models.Users;
import org.asturias.Infrastructure.Entities.UsersEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "createAt", target = "createAt"),
            @Mapping(source = "updateAt", target = "updateAt"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "documentNumber", target = "documentNumber"),
            @Mapping(source = "enabled", target = "enabled"),
            @Mapping(source = "lastLogin", target = "lastLogin"),
    })
    Users USERS(UsersEntity usersEntity);

    List<Users> USERS_LIST(List<UsersEntity> usersEntities);

    @InheritInverseConfiguration
    UsersEntity  USERS_ENTITY(Users users);


}
