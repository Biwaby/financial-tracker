package com.biwaby.financialtracker.mapper;

import com.biwaby.financialtracker.dto.read.UserReadDto;
import com.biwaby.financialtracker.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "roleName", source = "user.role.name")
    UserReadDto toDto(User user);
}
