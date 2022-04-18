package com.shoparoo.service;

import com.shoparoo.dto.BrandDto;
import com.shoparoo.entity.Brand;
import com.shoparoo.exception.BrandNotFoundException;
import com.shoparoo.mapper.MapStructMapper;
import com.shoparoo.mapper.MapStructMapperUpdate;
import com.shoparoo.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class BrandServiceTest {
    @InjectMocks
    BrandServiceImpl brandService;

    @Mock
    BrandRepository brandRepository;

    @Mock
    Environment environment;

    @Spy
    MapStructMapper mapStructMapper = Mappers.getMapper(MapStructMapper.class);

    @Spy
    MapStructMapperUpdate mapStructMapperUpdate = Mappers.getMapper(MapStructMapperUpdate.class);

    @Test
    void shouldReturnListOfAllBrands() {
        Brand brandA = new Brand(1L, "Brand A", true);
        Brand brandB = new Brand(2L, "Brand B", true);

        List<Brand> allBrands = List.of(brandA, brandB);

        when(brandRepository.findAll()).thenReturn(allBrands);

        List<Brand> returnedBrands = brandService.getAllBrands();

        assertThat(returnedBrands.size()).isEqualTo(2);
    }

    @Test
    void shouldReturnPageOfAllBrands() {
        Brand brandA = new Brand(1L, "Brand A", true);
        Brand brandB = new Brand(2L, "Brand B", true);

        Page<Brand> actualPage = new PageImpl<>(List.of(brandA, brandB));

        when(brandRepository.findAll(any(Pageable.class))).thenReturn(actualPage);

        Page<Brand> returnedPage = brandService.getAllBrandsPageable(1, 10);

        assertThat(returnedPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    void shouldReturnBrand_whenIdIsGiven() {
        Brand brandA = new Brand(1L, "Brand A", true);

        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(brandA));

        Brand returnedBrand = brandService.getBrandById(1L);

        assertThat(returnedBrand).isEqualTo(brandA);
    }

    @Test
    void shouldReturnBrand_whenNameIsGiven() {
        Brand brandA = new Brand(1L, "Brand A", true);

        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(brandA));

        Brand returnedBrand = brandService.getBrandByName("Brand A");

        assertThat(returnedBrand).isEqualTo(brandA);
    }

    @Test
    void shouldSaveNewBrand_whenValidInputsAreGiven() {
        BrandDto brandDto = new BrandDto("Brand A", true);

        when(brandRepository.findByName(anyString())).thenThrow(BrandNotFoundException.class);

        brandService.createNewBrand(brandDto, null);

        verify(brandRepository).save(any(Brand.class));
    }

    @Test
    void shouldUpdateBrand_whenValidInputsAreGiven() {
        Brand brand = new Brand(1L, "Brand A", true);
        BrandDto brandDto = new BrandDto("Brand B");

        when(brandRepository.findBrandsByName(anyString())).thenReturn(List.of(brand));
        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(brand));

        brandService.updateBrand(1L, brandDto, null);

        verify(brandRepository).save(any(Brand.class));
    }

    @Test
    void shouldDeleteBrand_whenValidIdIsGiven() {
        Brand brand = new Brand(1L, "Brand A", true);

        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(brand));

        brandService.deleteBrandById(1L);

        verify(brandRepository, times(1)).deleteById(anyLong());
    }
}