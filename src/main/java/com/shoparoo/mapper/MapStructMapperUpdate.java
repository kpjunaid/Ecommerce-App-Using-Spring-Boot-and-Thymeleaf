package com.shoparoo.mapper;

import com.shoparoo.dto.*;
import com.shoparoo.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MapStructMapperUpdate {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "accountVerified", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "address", ignore = true)
    void updateUserFromUserDto(UserDto userDto, @MappingTarget User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateAddressFromAddressDto(AddressDto addressDto, @MappingTarget Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "accountVerified", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "address", ignore = true)
    void adminUpdateUserFromUserDto(UserDto userDto, @MappingTarget User user);

    @Mapping(target = "id", ignore = true)
    void adminUpdateCategoryFromCategoryDto(CategoryDto categoryDto, @MappingTarget Category category);

    @Mapping(target = "id", ignore = true)
    void adminUpdateBrandFromBrandDto(BrandDto brandDto, @MappingTarget Brand brand);

    @Mapping(target = "id", ignore = true)
    void adminUpdateProductFromProductDto(ProductDto productDto, @MappingTarget Product product);
}
