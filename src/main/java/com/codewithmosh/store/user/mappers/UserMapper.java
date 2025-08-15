package com.codewithmosh.store.user.mappers;

import com.codewithmosh.store.auth.dtos.UserDto;
import com.codewithmosh.store.user.dtos.CreateUserDto;
import com.codewithmosh.store.user.dtos.UpdateUserDto;
import com.codewithmosh.store.user.entities.User;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(CreateUserDto user);
    void update(UpdateUserDto userDto, @MappingTarget User userEntity);
}
