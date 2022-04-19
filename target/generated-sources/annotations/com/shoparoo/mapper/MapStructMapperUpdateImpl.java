package com.shoparoo.mapper;

import com.shoparoo.dto.AddressDto;
import com.shoparoo.dto.BrandDto;
import com.shoparoo.dto.CategoryDto;
import com.shoparoo.dto.ProductDto;
import com.shoparoo.dto.UserDto;
import com.shoparoo.entity.Address;
import com.shoparoo.entity.Brand;
import com.shoparoo.entity.Category;
import com.shoparoo.entity.Product;
import com.shoparoo.entity.ProductPhoto;
import com.shoparoo.entity.Role;
import com.shoparoo.entity.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-19T06:01:55+0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.13 (Ubuntu)"
)
@Component
public class MapStructMapperUpdateImpl implements MapStructMapperUpdate {

    @Override
    public void updateUserFromUserDto(UserDto userDto, User user) {
        if ( userDto == null ) {
            return;
        }

        if ( userDto.getFirstName() != null ) {
            user.setFirstName( userDto.getFirstName() );
        }
        else {
            user.setFirstName( null );
        }
        if ( userDto.getLastName() != null ) {
            user.setLastName( userDto.getLastName() );
        }
        else {
            user.setLastName( null );
        }
        if ( userDto.getGender() != null ) {
            user.setGender( userDto.getGender() );
        }
        else {
            user.setGender( null );
        }
        if ( userDto.getPhotoUrl() != null ) {
            user.setPhotoUrl( userDto.getPhotoUrl() );
        }
        else {
            user.setPhotoUrl( null );
        }
        if ( userDto.getBirthDate() != null ) {
            user.setBirthDate( userDto.getBirthDate() );
        }
        else {
            user.setBirthDate( null );
        }
    }

    @Override
    public void updateAddressFromAddressDto(AddressDto addressDto, Address address) {
        if ( addressDto == null ) {
            return;
        }

        if ( addressDto.getAddressLineOne() != null ) {
            address.setAddressLineOne( addressDto.getAddressLineOne() );
        }
        else {
            address.setAddressLineOne( null );
        }
        if ( addressDto.getAddressLineTwo() != null ) {
            address.setAddressLineTwo( addressDto.getAddressLineTwo() );
        }
        else {
            address.setAddressLineTwo( null );
        }
        if ( addressDto.getCity() != null ) {
            address.setCity( addressDto.getCity() );
        }
        else {
            address.setCity( null );
        }
        if ( addressDto.getState() != null ) {
            address.setState( addressDto.getState() );
        }
        else {
            address.setState( null );
        }
        if ( addressDto.getZipCode() != null ) {
            address.setZipCode( addressDto.getZipCode() );
        }
        else {
            address.setZipCode( null );
        }
        if ( addressDto.getCountry() != null ) {
            address.setCountry( addressDto.getCountry() );
        }
        else {
            address.setCountry( null );
        }
    }

    @Override
    public void adminUpdateUserFromUserDto(UserDto userDto, User user) {
        if ( userDto == null ) {
            return;
        }

        if ( userDto.getFirstName() != null ) {
            user.setFirstName( userDto.getFirstName() );
        }
        else {
            user.setFirstName( null );
        }
        if ( userDto.getLastName() != null ) {
            user.setLastName( userDto.getLastName() );
        }
        else {
            user.setLastName( null );
        }
        if ( userDto.getGender() != null ) {
            user.setGender( userDto.getGender() );
        }
        else {
            user.setGender( null );
        }
        if ( userDto.getPhotoUrl() != null ) {
            user.setPhotoUrl( userDto.getPhotoUrl() );
        }
        else {
            user.setPhotoUrl( null );
        }
        if ( userDto.getBirthDate() != null ) {
            user.setBirthDate( userDto.getBirthDate() );
        }
        else {
            user.setBirthDate( null );
        }
        if ( userDto.getEnabled() != null ) {
            user.setEnabled( userDto.getEnabled() );
        }
        else {
            user.setEnabled( null );
        }
        if ( user.getRoles() != null ) {
            Set<Role> set = userDto.getRoles();
            if ( set != null ) {
                user.getRoles().clear();
                user.getRoles().addAll( set );
            }
        }
        else {
            Set<Role> set = userDto.getRoles();
            if ( set != null ) {
                user.setRoles( new HashSet<Role>( set ) );
            }
        }
    }

    @Override
    public void adminUpdateCategoryFromCategoryDto(CategoryDto categoryDto, Category category) {
        if ( categoryDto == null ) {
            return;
        }

        if ( categoryDto.getName() != null ) {
            category.setName( categoryDto.getName() );
        }
        else {
            category.setName( null );
        }
        if ( categoryDto.getDescription() != null ) {
            category.setDescription( categoryDto.getDescription() );
        }
        else {
            category.setDescription( null );
        }
        if ( categoryDto.getPhotoUrl() != null ) {
            category.setPhotoUrl( categoryDto.getPhotoUrl() );
        }
        else {
            category.setPhotoUrl( null );
        }
        if ( categoryDto.getEnabled() != null ) {
            category.setEnabled( categoryDto.getEnabled() );
        }
        else {
            category.setEnabled( null );
        }
        if ( categoryDto.getParentCategory() != null ) {
            category.setParentCategory( categoryDto.getParentCategory() );
        }
        else {
            category.setParentCategory( null );
        }
        if ( category.getSubCategories() != null ) {
            List<Category> list = categoryDto.getSubCategories();
            if ( list != null ) {
                category.getSubCategories().clear();
                category.getSubCategories().addAll( list );
            }
        }
        else {
            List<Category> list = categoryDto.getSubCategories();
            if ( list != null ) {
                category.setSubCategories( new ArrayList<Category>( list ) );
            }
        }
        if ( category.getBrands() != null ) {
            Set<Brand> set = categoryDto.getBrands();
            if ( set != null ) {
                category.getBrands().clear();
                category.getBrands().addAll( set );
            }
        }
        else {
            Set<Brand> set = categoryDto.getBrands();
            if ( set != null ) {
                category.setBrands( new HashSet<Brand>( set ) );
            }
        }
    }

    @Override
    public void adminUpdateBrandFromBrandDto(BrandDto brandDto, Brand brand) {
        if ( brandDto == null ) {
            return;
        }

        if ( brandDto.getName() != null ) {
            brand.setName( brandDto.getName() );
        }
        else {
            brand.setName( null );
        }
        if ( brandDto.getDescription() != null ) {
            brand.setDescription( brandDto.getDescription() );
        }
        else {
            brand.setDescription( null );
        }
        if ( brandDto.getPhotoUrl() != null ) {
            brand.setPhotoUrl( brandDto.getPhotoUrl() );
        }
        else {
            brand.setPhotoUrl( null );
        }
        if ( brandDto.getEnabled() != null ) {
            brand.setEnabled( brandDto.getEnabled() );
        }
        else {
            brand.setEnabled( null );
        }
        if ( brand.getCategories() != null ) {
            List<Category> list = brandDto.getCategories();
            if ( list != null ) {
                brand.getCategories().clear();
                brand.getCategories().addAll( list );
            }
        }
        else {
            List<Category> list = brandDto.getCategories();
            if ( list != null ) {
                brand.setCategories( new HashSet<Category>( list ) );
            }
        }
    }

    @Override
    public void adminUpdateProductFromProductDto(ProductDto productDto, Product product) {
        if ( productDto == null ) {
            return;
        }

        if ( productDto.getName() != null ) {
            product.setName( productDto.getName() );
        }
        else {
            product.setName( null );
        }
        if ( productDto.getShortDescription() != null ) {
            product.setShortDescription( productDto.getShortDescription() );
        }
        else {
            product.setShortDescription( null );
        }
        if ( productDto.getDescription() != null ) {
            product.setDescription( productDto.getDescription() );
        }
        else {
            product.setDescription( null );
        }
        product.setCostPrice( productDto.getCostPrice() );
        product.setResalePrice( productDto.getResalePrice() );
        if ( productDto.getQuantity() != null ) {
            product.setQuantity( productDto.getQuantity() );
        }
        else {
            product.setQuantity( null );
        }
        if ( productDto.getInStock() != null ) {
            product.setInStock( productDto.getInStock() );
        }
        else {
            product.setInStock( null );
        }
        if ( productDto.getDiscount() != null ) {
            product.setDiscount( productDto.getDiscount() );
        }
        else {
            product.setDiscount( null );
        }
        if ( productDto.getEnabled() != null ) {
            product.setEnabled( productDto.getEnabled() );
        }
        else {
            product.setEnabled( null );
        }
        if ( product.getProductPhotos() != null ) {
            List<ProductPhoto> list = productDto.getProductPhotos();
            if ( list != null ) {
                product.getProductPhotos().clear();
                product.getProductPhotos().addAll( list );
            }
        }
        else {
            List<ProductPhoto> list = productDto.getProductPhotos();
            if ( list != null ) {
                product.setProductPhotos( new ArrayList<ProductPhoto>( list ) );
            }
        }
        if ( productDto.getCategory() != null ) {
            product.setCategory( productDto.getCategory() );
        }
        else {
            product.setCategory( null );
        }
        if ( productDto.getBrand() != null ) {
            product.setBrand( productDto.getBrand() );
        }
        else {
            product.setBrand( null );
        }
    }
}
