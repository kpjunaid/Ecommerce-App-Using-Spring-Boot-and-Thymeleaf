package com.shoparoo.service;

import com.shoparoo.dto.BrandDto;
import com.shoparoo.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BrandService {
    List<Brand> getAllBrands();
    Page<Brand> getAllBrandsPageable(Integer pageNumber, Integer contentPerPage);
    Brand getBrandById(Long id);
    Brand getBrandByName(String name);
    void createNewBrand(BrandDto brandDto, MultipartFile brandPhoto);
    void updateBrand(Long id, BrandDto brandDto, MultipartFile brandPhoto);
    void deleteBrandById(Long id);
}
