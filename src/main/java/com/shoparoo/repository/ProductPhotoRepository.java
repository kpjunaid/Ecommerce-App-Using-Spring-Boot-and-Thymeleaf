package com.shoparoo.repository;

import com.shoparoo.entity.Product;
import com.shoparoo.entity.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {
    List<ProductPhoto> findProductPhotosByProduct(Product product);
    Integer countProductPhotosByProduct(Product product);
}
