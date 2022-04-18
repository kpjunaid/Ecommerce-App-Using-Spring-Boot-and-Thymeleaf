package com.shoparoo.service;

import com.shoparoo.dto.BrandDto;
import com.shoparoo.dto.CategoryDto;
import com.shoparoo.dto.ProductDto;
import com.shoparoo.entity.Brand;
import com.shoparoo.entity.Category;
import com.shoparoo.entity.Product;
import com.shoparoo.entity.ProductPhoto;
import com.shoparoo.exception.ProductNotFoundException;
import com.shoparoo.exception.ProductPhotoLimitException;
import com.shoparoo.mapper.MapStructMapper;
import com.shoparoo.mapper.MapStructMapperUpdate;
import com.shoparoo.repository.ProductRepository;
import com.shoparoo.util.FileUploadUtil;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductPhotoService productPhotoService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final MapStructMapper mapStructMapper;
    private final MapStructMapperUpdate mapStructMapperUpdate;
    private final Environment environment;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductPhotoService productPhotoService,
                              CategoryService categoryService,
                              BrandService brandService,
                              MapStructMapper mapStructMapper,
                              MapStructMapperUpdate mapStructMapperUpdate,
                              Environment environment) {
        this.productRepository = productRepository;
        this.productPhotoService = productPhotoService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.mapStructMapper = mapStructMapper;
        this.mapStructMapperUpdate = mapStructMapperUpdate;
        this.environment = environment;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllProductsByCategoryPageable(Integer pageNumber, Long categoryId, String sort, String dir, Integer contentPerPage) {
        if (pageNumber <= 0) {
            return Page.empty();
        }

        Sort.Direction sortDir;
        if (dir.equalsIgnoreCase("asc")) {
            sortDir = Sort.Direction.ASC;
        } else if (dir.equalsIgnoreCase("desc")) {
            sortDir = Sort.Direction.DESC;
        } else {
            sortDir = Sort.Direction.ASC;
        }

        try {
            Category category = categoryService.getCategoryById(categoryId);

            Pageable pageable = PageRequest.of(pageNumber - 1, contentPerPage, Sort.by(sortDir, sort));
            return productRepository.findProductsByCategory(category, pageable);
        } catch (RuntimeException e) {
            return Page.empty();
        }
    }

    @Override
    public Page<Product> getAllProductsByBrandPageable(Integer pageNumber, Long brandId, String sort, String dir, Integer contentPerPage) {
        if (pageNumber <= 0) {
            return Page.empty();
        }

        Sort.Direction sortDir;
        if (dir.equalsIgnoreCase("asc")) {
            sortDir = Sort.Direction.ASC;
        } else if (dir.equalsIgnoreCase("desc")) {
            sortDir = Sort.Direction.DESC;
        } else {
            sortDir = Sort.Direction.ASC;
        }

        try {
            Brand brand = brandService.getBrandById(brandId);
            Pageable pageable = PageRequest.of(pageNumber - 1, contentPerPage, Sort.by(sortDir, sort));
            return productRepository.findProductsByBrand(brand, pageable);
        } catch (RuntimeException e) {
            return Page.empty();
        }
    }

    @Override
    public Page<Product> searchProductsPageable(Integer pageNumber, String search, String sort, String dir, Integer contentPerPage) {
        if (pageNumber <= 0) {
            return Page.empty();
        }

        Sort.Direction sortDir;
        if (dir.equalsIgnoreCase("asc")) {
            sortDir = Sort.Direction.ASC;
        } else if (dir.equalsIgnoreCase("desc")) {
            sortDir = Sort.Direction.DESC;
        } else {
            sortDir = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, contentPerPage, Sort.by(sortDir, sort));
        return productRepository.findProductsByNameContainsIgnoreCase(search, pageable);
    }

    @Override
    public Page<Product> getAllProductsPageable(Integer pageNumber, String sort, String dir, Integer contentPerPage) {
        if (pageNumber <= 0) {
            return Page.empty();
        }

        Sort.Direction sortDir;
        if (dir.equalsIgnoreCase("asc")) {
            sortDir = Sort.Direction.ASC;
        } else if (dir.equalsIgnoreCase("desc")) {
            sortDir = Sort.Direction.DESC;
        } else {
            sortDir = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, contentPerPage, Sort.by(sortDir, sort));
        Page<Product> foundPage = productRepository.findAll(pageable);

        if (foundPage.hasContent()) {
            return foundPage;
        } else {
            return Page.empty();
        }
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    @Transactional
    public void createNewProduct(ProductDto productDto, List<Optional<MultipartFile>> productPhotos) {
        Product product = mapStructMapper.productDtoToProduct(productDto);
        Product savedProduct = productRepository.save(product);

        if (productPhotos != null) {
            for (Optional<MultipartFile> productPhoto : productPhotos) {
                if (productPhoto.isPresent() && !productPhoto.get().isEmpty()) {
                    ProductPhoto savedProductPhoto = productPhotoService.saveNewProductPhoto(productPhoto.get(), savedProduct);
                    productDto.getProductPhotos().add(savedProductPhoto);
                }
            }
        }
    }

    @Override
    public void updateProduct(Long id, ProductDto productDto, List<Optional<MultipartFile>> productPhotos) {
        try {
            Product product = getProductById(id);
            Integer totalProductPhotoCount = productPhotoService.getProductPhotoCountByProduct(product);
            for (Optional<MultipartFile> productPhoto: productPhotos) {
                if (productPhoto.isPresent() && productPhoto.get().getSize() > 0) {
                    totalProductPhotoCount++;
                }
            }
            if (totalProductPhotoCount > 3) {
                throw new ProductPhotoLimitException();
            }

            mapStructMapperUpdate.adminUpdateProductFromProductDto(productDto, product);
            productRepository.save(product);

            if (productPhotos != null) {
                for (Optional<MultipartFile> productPhoto : productPhotos) {
                    if (productPhoto.isPresent() && !productPhoto.get().isEmpty()) {
                        ProductPhoto savedProductPhoto = productPhotoService.saveNewProductPhoto(productPhoto.get(), product);
                        productDto.getProductPhotos().add(savedProductPhoto);
                    }
                }
            }
        } catch (ProductNotFoundException e) {
            throw new ProductNotFoundException();
        }
    }

    @Override
    public void deleteProductById(Long id) {
        try {
            Product product = getProductById(id);
            if (product != null) {
                List<ProductPhoto> productPhotos = product.getProductPhotos();
                // delete product photos from filesystem if exist
                if (productPhotos != null && productPhotos.size() > 0) {
                    String photoUploadDir = environment.getProperty("photo.upload.product");
                    for (ProductPhoto productPhoto : productPhotos) {
                        try {
                            FileUploadUtil.deleteFile(photoUploadDir, productPhoto.getPhotoUrl());
                        } catch (IOException e) {
                            throw new RuntimeException();
                        }
                    }
                }

                productRepository.deleteById(id);
            }
        } catch (ProductNotFoundException e) {
            throw new ProductNotFoundException();
        }
    }
}
