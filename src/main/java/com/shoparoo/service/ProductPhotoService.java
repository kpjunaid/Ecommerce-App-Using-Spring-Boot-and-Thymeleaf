package com.shoparoo.service;

import com.shoparoo.entity.Product;
import com.shoparoo.entity.ProductPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductPhotoService {
    ProductPhoto getProductById(Long id);
    List<ProductPhoto> getProductPhotosByProduct(Product product);
    ProductPhoto saveNewProductPhoto(MultipartFile productPhoto, Product product);
    void deleteProductPhotoById(Long id);
    Integer getProductPhotoCountByProduct(Product product);
}
