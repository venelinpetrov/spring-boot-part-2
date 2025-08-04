package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface CartItemMapper {
    @Mapping(source = "product", target = "product")
    CartItemDto toDto(CartItem item);
}
