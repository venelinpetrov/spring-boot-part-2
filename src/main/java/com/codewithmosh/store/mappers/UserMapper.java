package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CreateUserDto;
import com.codewithmosh.store.dtos.UpdateUserDto;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(CreateUserDto user);
    void update(UpdateUserDto userDto, @MappingTarget User userEntity);
}
