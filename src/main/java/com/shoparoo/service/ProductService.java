package com.shoparoo.service;

import com.shoparoo.dto.BrandDto;
import com.shoparoo.dto.CategoryDto;
import com.shoparoo.dto.ProductDto;
import com.shoparoo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Page<Product> getAllProductsPageable(Integer pageNumber, String sort, String dir, Integer contentPerPage);
    Page<Product> getAllProductsByCategoryPageable(Integer pageNumber, Long categoryId, String sort, String dir, Integer contentPerPage);
    Page<Product> getAllProductsByBrandPageable(Integer pageNumber, Long brandId, String sort, String dir, Integer contentPerPage);
    Page<Product> searchProductsPageable(Integer pageNumber, String search, String sort, String dir, Integer contentPerPage);
    Product getProductById(Long id);
    void createNewProduct(ProductDto productDto, List<Optional<MultipartFile>> productPhotos);
    void updateProduct(Long id, ProductDto productDto, List<Optional<MultipartFile>> productPhotos);
    void deleteProductById(Long id);
}
