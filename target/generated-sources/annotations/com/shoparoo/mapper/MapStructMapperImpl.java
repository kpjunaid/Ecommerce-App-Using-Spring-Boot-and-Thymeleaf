package com.shoparoo.mapper;

import com.shoparoo.dto.AddressDto;
import com.shoparoo.dto.AdminUserUpdateDto;
import com.shoparoo.dto.BrandDto;
import com.shoparoo.dto.CartItemDto;
import com.shoparoo.dto.CategoryDto;
import com.shoparoo.dto.CountryDto;
import com.shoparoo.dto.ProductDto;
import com.shoparoo.dto.ProductPhotoDto;
import com.shoparoo.dto.RoleDto;
import com.shoparoo.dto.UserDto;
import com.shoparoo.dto.UserUpdateDto;
import com.shoparoo.entity.Address;
import com.shoparoo.entity.Brand;
import com.shoparoo.entity.CartItem;
import com.shoparoo.entity.Category;
import com.shoparoo.entity.Country;
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
public class MapStructMapperImpl implements MapStructMapper {

    @Override
    public User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDto.getId() );
        user.setEmail( userDto.getEmail() );
        user.setPassword( userDto.getPassword() );
        user.setFirstName( userDto.getFirstName() );
        user.setLastName( userDto.getLastName() );
        user.setGender( userDto.getGender() );
        user.setPhotoUrl( userDto.getPhotoUrl() );
        user.setBirthDate( userDto.getBirthDate() );
        user.setEnabled( userDto.getEnabled() );
        user.setAccountVerified( userDto.getAccountVerified() );
        user.setEmailVerified( userDto.getEmailVerified() );
        Set<Role> set = userDto.getRoles();
        if ( set != null ) {
            user.setRoles( new HashSet<Role>( set ) );
        }
        user.setAddress( userDto.getAddress() );

        return user;
    }

    @Override
    public UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setEmail( user.getEmail() );
        userDto.setFirstName( user.getFirstName() );
        userDto.setLastName( user.getLastName() );
        userDto.setGender( user.getGender() );
        userDto.setEnabled( user.getEnabled() );
        userDto.setAccountVerified( user.getAccountVerified() );
        userDto.setEmailVerified( user.getEmailVerified() );
        userDto.setPhotoUrl( user.getPhotoUrl() );
        userDto.setBirthDate( user.getBirthDate() );
        Set<Role> set = user.getRoles();
        if ( set != null ) {
            userDto.setRoles( new HashSet<Role>( set ) );
        }
        userDto.setAddress( user.getAddress() );

        return userDto;
    }

    @Override
    public UserDto userUpdateDtoToUserDto(UserUpdateDto userUpdateDto) {
        if ( userUpdateDto == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setFirstName( userUpdateDto.getFirstName() );
        userDto.setLastName( userUpdateDto.getLastName() );
        userDto.setGender( userUpdateDto.getGender() );
        userDto.setPhotoUrl( userUpdateDto.getPhotoUrl() );
        userDto.setBirthDate( userUpdateDto.getBirthDate() );

        return userDto;
    }

    @Override
    public UserDto adminUserUpdateDtoToUserDto(AdminUserUpdateDto adminUserUpdateDto) {
        if ( adminUserUpdateDto == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setFirstName( adminUserUpdateDto.getFirstName() );
        userDto.setLastName( adminUserUpdateDto.getLastName() );
        userDto.setGender( adminUserUpdateDto.getGender() );
        userDto.setEnabled( adminUserUpdateDto.getEnabled() );
        userDto.setPhotoUrl( adminUserUpdateDto.getPhotoUrl() );
        userDto.setBirthDate( adminUserUpdateDto.getBirthDate() );
        Set<Role> set = adminUserUpdateDto.getRoles();
        if ( set != null ) {
            userDto.setRoles( new HashSet<Role>( set ) );
        }

        return userDto;
    }

    @Override
    public Role roleDtoToRole(RoleDto roleDto) {
        if ( roleDto == null ) {
            return null;
        }

        Role role = new Role();

        role.setId( roleDto.getId() );

        return role;
    }

    @Override
    public RoleDto roleToRoleDto(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleDto roleDto = new RoleDto();

        roleDto.setId( role.getId() );

        return roleDto;
    }

    @Override
    public Address addressDtoToAddress(AddressDto addressDto) {
        if ( addressDto == null ) {
            return null;
        }

        Address address = new Address();

        address.setId( addressDto.getId() );
        address.setAddressLineOne( addressDto.getAddressLineOne() );
        address.setAddressLineTwo( addressDto.getAddressLineTwo() );
        address.setCity( addressDto.getCity() );
        address.setState( addressDto.getState() );
        address.setZipCode( addressDto.getZipCode() );
        address.setCountry( addressDto.getCountry() );
        address.setUser( addressDto.getUser() );

        return address;
    }

    @Override
    public AddressDto addressToAddressDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDto addressDto = new AddressDto();

        addressDto.setId( address.getId() );
        addressDto.setAddressLineOne( address.getAddressLineOne() );
        addressDto.setAddressLineTwo( address.getAddressLineTwo() );
        addressDto.setCity( address.getCity() );
        addressDto.setState( address.getState() );
        addressDto.setZipCode( address.getZipCode() );
        addressDto.setCountry( address.getCountry() );
        addressDto.setUser( address.getUser() );

        return addressDto;
    }

    @Override
    public AddressDto userUpdateDtoToAddressDto(UserUpdateDto userUpdateDto) {
        if ( userUpdateDto == null ) {
            return null;
        }

        AddressDto addressDto = new AddressDto();

        addressDto.setAddressLineOne( userUpdateDto.getAddressLineOne() );
        addressDto.setAddressLineTwo( userUpdateDto.getAddressLineTwo() );
        addressDto.setCity( userUpdateDto.getCity() );
        addressDto.setState( userUpdateDto.getState() );
        addressDto.setZipCode( userUpdateDto.getZipCode() );
        addressDto.setCountry( userUpdateDto.getCountry() );

        return addressDto;
    }

    @Override
    public Country countryDtoToCountry(CountryDto countryDto) {
        if ( countryDto == null ) {
            return null;
        }

        Country country = new Country();

        country.setId( countryDto.getId() );
        country.setName( countryDto.getName() );
        country.setOfficialName( countryDto.getOfficialName() );
        country.setCapital( countryDto.getCapital() );
        country.setCountryCode( countryDto.getCountryCode() );
        country.setCallingCode( countryDto.getCallingCode() );
        country.setCurrencyName( countryDto.getCurrencyName() );
        country.setCurrencyCode( countryDto.getCurrencyCode() );
        country.setCurrencySymbol( countryDto.getCurrencySymbol() );
        country.setFlagUrl( countryDto.getFlagUrl() );

        return country;
    }

    @Override
    public CountryDto countryToCountryDto(Country country) {
        if ( country == null ) {
            return null;
        }

        CountryDto countryDto = new CountryDto();

        countryDto.setId( country.getId() );
        countryDto.setName( country.getName() );
        countryDto.setOfficialName( country.getOfficialName() );
        countryDto.setCapital( country.getCapital() );
        countryDto.setCallingCode( country.getCallingCode() );
        countryDto.setCountryCode( country.getCountryCode() );
        countryDto.setCurrencyName( country.getCurrencyName() );
        countryDto.setCurrencyCode( country.getCurrencyCode() );
        countryDto.setCurrencySymbol( country.getCurrencySymbol() );
        countryDto.setFlagUrl( country.getFlagUrl() );

        return countryDto;
    }

    @Override
    public Category categoryDtoToCategory(CategoryDto categoryDto) {
        if ( categoryDto == null ) {
            return null;
        }

        Category category = new Category();

        category.setId( categoryDto.getId() );
        category.setName( categoryDto.getName() );
        category.setDescription( categoryDto.getDescription() );
        category.setPhotoUrl( categoryDto.getPhotoUrl() );
        category.setEnabled( categoryDto.getEnabled() );
        category.setParentCategory( categoryDto.getParentCategory() );
        List<Category> list = categoryDto.getSubCategories();
        if ( list != null ) {
            category.setSubCategories( new ArrayList<Category>( list ) );
        }
        Set<Brand> set = categoryDto.getBrands();
        if ( set != null ) {
            category.setBrands( new HashSet<Brand>( set ) );
        }

        return category;
    }

    @Override
    public CategoryDto categoryToCategoryDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId( category.getId() );
        categoryDto.setName( category.getName() );
        categoryDto.setDescription( category.getDescription() );
        categoryDto.setPhotoUrl( category.getPhotoUrl() );
        categoryDto.setEnabled( category.getEnabled() );
        categoryDto.setParentCategory( category.getParentCategory() );
        List<Category> list = category.getSubCategories();
        if ( list != null ) {
            categoryDto.setSubCategories( new ArrayList<Category>( list ) );
        }
        Set<Brand> set = category.getBrands();
        if ( set != null ) {
            categoryDto.setBrands( new HashSet<Brand>( set ) );
        }

        return categoryDto;
    }

    @Override
    public Brand brandDtoToBrand(BrandDto brandDto) {
        if ( brandDto == null ) {
            return null;
        }

        Brand brand = new Brand();

        brand.setId( brandDto.getId() );
        brand.setName( brandDto.getName() );
        brand.setDescription( brandDto.getDescription() );
        brand.setPhotoUrl( brandDto.getPhotoUrl() );
        brand.setEnabled( brandDto.getEnabled() );
        List<Category> list = brandDto.getCategories();
        if ( list != null ) {
            brand.setCategories( new HashSet<Category>( list ) );
        }

        return brand;
    }

    @Override
    public BrandDto brandToBrandDto(Brand brand) {
        if ( brand == null ) {
            return null;
        }

        BrandDto brandDto = new BrandDto();

        brandDto.setId( brand.getId() );
        brandDto.setName( brand.getName() );
        brandDto.setDescription( brand.getDescription() );
        brandDto.setPhotoUrl( brand.getPhotoUrl() );
        brandDto.setEnabled( brand.getEnabled() );
        Set<Category> set = brand.getCategories();
        if ( set != null ) {
            brandDto.setCategories( new ArrayList<Category>( set ) );
        }

        return brandDto;
    }

    @Override
    public Product productDtoToProduct(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product product = new Product();

        product.setId( productDto.getId() );
        product.setName( productDto.getName() );
        product.setShortDescription( productDto.getShortDescription() );
        product.setDescription( productDto.getDescription() );
        product.setCostPrice( productDto.getCostPrice() );
        product.setResalePrice( productDto.getResalePrice() );
        product.setQuantity( productDto.getQuantity() );
        product.setInStock( productDto.getInStock() );
        product.setDiscount( productDto.getDiscount() );
        product.setEnabled( productDto.getEnabled() );
        List<ProductPhoto> list = productDto.getProductPhotos();
        if ( list != null ) {
            product.setProductPhotos( new ArrayList<ProductPhoto>( list ) );
        }
        product.setCategory( productDto.getCategory() );
        product.setBrand( productDto.getBrand() );

        return product;
    }

    @Override
    public ProductDto productToProductDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setId( product.getId() );
        productDto.setName( product.getName() );
        productDto.setShortDescription( product.getShortDescription() );
        productDto.setDescription( product.getDescription() );
        productDto.setCostPrice( product.getCostPrice() );
        productDto.setResalePrice( product.getResalePrice() );
        productDto.setQuantity( product.getQuantity() );
        productDto.setDiscount( product.getDiscount() );
        productDto.setInStock( product.getInStock() );
        productDto.setEnabled( product.getEnabled() );
        List<ProductPhoto> list = product.getProductPhotos();
        if ( list != null ) {
            productDto.setProductPhotos( new ArrayList<ProductPhoto>( list ) );
        }
        productDto.setCategory( product.getCategory() );
        productDto.setBrand( product.getBrand() );

        return productDto;
    }

    @Override
    public ProductPhoto productPhotoDtoToProductPhoto(ProductPhotoDto productPhotoDto) {
        if ( productPhotoDto == null ) {
            return null;
        }

        ProductPhoto productPhoto = new ProductPhoto();

        productPhoto.setId( productPhotoDto.getId() );
        productPhoto.setPhotoUrl( productPhotoDto.getPhotoUrl() );
        productPhoto.setProduct( productPhotoDto.getProduct() );

        return productPhoto;
    }

    @Override
    public ProductPhotoDto productPhotoToProductPhotoDto(ProductPhoto productPhoto) {
        if ( productPhoto == null ) {
            return null;
        }

        ProductPhotoDto productPhotoDto = new ProductPhotoDto();

        productPhotoDto.setId( productPhoto.getId() );
        productPhotoDto.setPhotoUrl( productPhoto.getPhotoUrl() );
        productPhotoDto.setProduct( productPhoto.getProduct() );

        return productPhotoDto;
    }

    @Override
    public CartItem cartItemDtoToCartItem(CartItemDto cartItemDto) {
        if ( cartItemDto == null ) {
            return null;
        }

        CartItem cartItem = new CartItem();

        cartItem.setId( cartItemDto.getId() );
        cartItem.setUser( cartItemDto.getUser() );
        cartItem.setProduct( cartItemDto.getProduct() );
        cartItem.setQuantity( cartItemDto.getQuantity() );

        return cartItem;
    }

    @Override
    public CartItem cartItemToCartItemDto(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        CartItem cartItem1 = new CartItem();

        cartItem1.setCreatedBy( cartItem.getCreatedBy() );
        cartItem1.setLastModifiedBy( cartItem.getLastModifiedBy() );
        cartItem1.setCreatedAt( cartItem.getCreatedAt() );
        cartItem1.setLastModifiedAt( cartItem.getLastModifiedAt() );
        cartItem1.setId( cartItem.getId() );
        cartItem1.setUser( cartItem.getUser() );
        cartItem1.setProduct( cartItem.getProduct() );
        cartItem1.setQuantity( cartItem.getQuantity() );

        return cartItem1;
    }
}
