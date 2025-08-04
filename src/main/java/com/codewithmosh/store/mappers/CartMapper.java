package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.entities.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { CartMapper.class })
public interface CartMapper {
    @Mapping(source = "items", target = "items")
    CartDto toDto(Cart cart);
}
