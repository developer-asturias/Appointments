//package org.asturias.Infrastructure.Mappers.Entities;
//
//import org.asturias.Domain.Models.Role;
//import org.asturias.Infrastructure.Entities.RoleEntity;
//import org.mapstruct.InheritInverseConfiguration;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Mappings;
//
//import java.util.List;
//import java.util.Set;
//
//@Mapper(componentModel = "spring")
//public interface RoleMapper {
//    @Mappings({
//            @Mapping(source = "id", target = "id"),
//            @Mapping(source = "name", target = "name"),
//    })
//    Role ROLE(RoleEntity roleEntity);
//
//    List<Role> ROLE_LIST(List<RoleEntity> roleEntities);
//
//    @InheritInverseConfiguration
//    RoleEntity  ROLE_ENTITY(Role role);
//
//    Set<Role> toRoleSet(Set<RoleEntity> roleEntities);
//}
