package com.biwaby.financialtracker.mapper;

import com.biwaby.financialtracker.dto.UserDto;
import com.biwaby.financialtracker.dto.UserEditDto;
import com.biwaby.financialtracker.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto toDto(User user);
    UserEditDto toEditDto(User user);
}
