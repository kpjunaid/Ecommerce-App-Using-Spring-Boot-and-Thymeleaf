package com.shoparoo.service;

import com.shoparoo.dto.BrandDto;
import com.shoparoo.entity.Brand;
import com.shoparoo.exception.BrandExistsException;
import com.shoparoo.exception.BrandNotFoundException;
import com.shoparoo.mapper.MapStructMapper;
import com.shoparoo.mapper.MapStructMapperUpdate;
import com.shoparoo.repository.BrandRepository;
import com.shoparoo.util.FileNamingUtil;
import com.shoparoo.util.FileUploadUtil;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final MapStructMapper mapStructMapper;
    private final MapStructMapperUpdate mapStructMapperUpdate;
    private final Environment environment;

    public BrandServiceImpl(BrandRepository brandRepository,
                            MapStructMapper mapStructMapper,
                            MapStructMapperUpdate mapStructMapperUpdate,
                            Environment environment) {
        this.brandRepository = brandRepository;
        this.mapStructMapper = mapStructMapper;
        this.mapStructMapperUpdate = mapStructMapperUpdate;
        this.environment = environment;
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Page<Brand> getAllBrandsPageable(Integer pageNumber, Integer contentPerPage) {
        if (pageNumber <= 0) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, contentPerPage, Sort.by("name").ascending());
        Page<Brand> foundPage = brandRepository.findAll(pageable);

        if (foundPage.hasContent()) {
            return foundPage;
        } else {
            return Page.empty();
        }
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElseThrow(BrandNotFoundException::new);
    }

    @Override
    public Brand getBrandByName(String name) {
        return brandRepository.findByName(name).orElseThrow(BrandNotFoundException::new);
    }

    @Override
    public void createNewBrand(BrandDto brandDto, MultipartFile brandPhoto) {
        try {
            Brand brand = getBrandByName(brandDto.getName());
            if (brand != null) {
                throw new BrandExistsException();
            }
        } catch (BrandNotFoundException e) {
            if (brandPhoto != null) {
                String photoUploadDir = environment.getProperty("photo.upload.brand");
                brandDto.setPhotoUrl(FileNamingUtil.nameFile(brandPhoto));
                try {
                    FileUploadUtil.saveNewFile(photoUploadDir, brandDto.getPhotoUrl(), brandPhoto);
                } catch (IOException ignored) {
                    throw new RuntimeException();
                }
            }

            Brand brand = mapStructMapper.brandDtoToBrand(brandDto);
            brandRepository.save(brand);
        }
    }

    @Override
    public void updateBrand(Long id, BrandDto brandDto, MultipartFile brandPhoto) {
        try {
            List<Brand> duplicateCategories = brandRepository.findBrandsByName(brandDto.getName());
            if (duplicateCategories.size() > 1) {
                throw new BrandExistsException();
            }

            Brand brand = getBrandById(id);

            if (brandPhoto != null) {
                String dpUploadDir = environment.getProperty("photo.upload.brand");
                brandDto.setPhotoUrl(FileNamingUtil.nameFile(brandPhoto));
                try {
                    if (brand.getPhotoUrl() == null) {
                        FileUploadUtil.saveNewFile(dpUploadDir, brandDto.getPhotoUrl(), brandPhoto);
                    } else {
                        FileUploadUtil.updateFile(dpUploadDir, brandDto.getPhotoUrl(),
                                brandDto.getPhotoUrl(), brandPhoto);
                    }
                } catch (IOException ignored) {
                    throw new RuntimeException();
                }
            } else {
                brandDto.setPhotoUrl(brand.getPhotoUrl());
            }

            mapStructMapperUpdate.adminUpdateBrandFromBrandDto(brandDto, brand);
            brandRepository.save(brand);
        } catch (BrandNotFoundException e) {
            throw new BrandNotFoundException();
        }
    }

    @Override
    public void deleteBrandById(Long id) {
        try {
            Brand brand = getBrandById(id);
            if (brand != null) {
                String photo = brand.getPhotoUrl();
                // delete brand photo from filesystem if exists
                if (photo != null && photo.length() > 0) {
                    String photoUploadDir = environment.getProperty("photo.upload.brand");
                    try {
                        FileUploadUtil.deleteFile(photoUploadDir, photo);
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                }

                brandRepository.deleteById(id);
            }
        } catch (BrandNotFoundException e) {
            throw new BrandNotFoundException();
        }
    }
}
