package com.shoparoo.mapper;

import com.shoparoo.dto.*;
import com.shoparoo.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
    User userDtoToUser(UserDto userDto);
    @Mapping(target = "password", ignore = true)
    UserDto userToUserDto(User user);
    UserDto userUpdateDtoToUserDto(UserUpdateDto userUpdateDto);
    UserDto adminUserUpdateDtoToUserDto(AdminUserUpdateDto adminUserUpdateDto);
    Role roleDtoToRole(RoleDto roleDto);
    RoleDto roleToRoleDto(Role role);
    Address addressDtoToAddress(AddressDto addressDto);
    AddressDto addressToAddressDto(Address address);
    AddressDto userUpdateDtoToAddressDto(UserUpdateDto userUpdateDto);
    Country countryDtoToCountry(CountryDto countryDto);
    CountryDto countryToCountryDto(Country country);
    Category categoryDtoToCategory(CategoryDto categoryDto);
    CategoryDto categoryToCategoryDto(Category category);
    Brand brandDtoToBrand(BrandDto brandDto);
    BrandDto brandToBrandDto(Brand brand);
    Product productDtoToProduct(ProductDto productDto);
    ProductDto productToProductDto(Product product);
    ProductPhoto productPhotoDtoToProductPhoto(ProductPhotoDto productPhotoDto);
    ProductPhotoDto productPhotoToProductPhotoDto(ProductPhoto productPhoto);
    CartItem cartItemDtoToCartItem(CartItemDto cartItemDto);
    CartItem cartItemToCartItemDto(CartItem cartItem);
}
