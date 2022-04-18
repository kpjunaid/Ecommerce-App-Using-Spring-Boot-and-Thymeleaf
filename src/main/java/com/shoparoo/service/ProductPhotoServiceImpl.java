package com.shoparoo.service;

import com.shoparoo.dto.ProductPhotoDto;
import com.shoparoo.entity.Product;
import com.shoparoo.entity.ProductPhoto;
import com.shoparoo.exception.ProductPhotoNotFoundException;
import com.shoparoo.mapper.MapStructMapper;
import com.shoparoo.repository.ProductPhotoRepository;
import com.shoparoo.util.FileNamingUtil;
import com.shoparoo.util.FileUploadUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductPhotoServiceImpl implements ProductPhotoService {
    private final ProductPhotoRepository productPhotoRepository;
    private final MapStructMapper mapStructMapper;
    private final Environment environment;

    public ProductPhotoServiceImpl(ProductPhotoRepository productPhotoRepository,
                                   MapStructMapper mapStructMapper,
                                   Environment environment) {
        this.productPhotoRepository = productPhotoRepository;
        this.mapStructMapper = mapStructMapper;
        this.environment = environment;
    }

    @Override
    public ProductPhoto getProductById(Long id) {
        return productPhotoRepository.findById(id).orElseThrow(ProductPhotoNotFoundException::new);
    }

    @Override
    public List<ProductPhoto> getProductPhotosByProduct(Product product) {
        return productPhotoRepository.findProductPhotosByProduct(product);
    }

    @Override
    public ProductPhoto saveNewProductPhoto(MultipartFile productPhoto, Product product) {
        String photoUploadDir = environment.getProperty("photo.upload.product");
        String photoName = FileNamingUtil.nameFile(productPhoto);
        try {
            FileUploadUtil.saveNewFile(photoUploadDir, photoName, productPhoto);
        } catch (IOException ignored) {
            throw new RuntimeException();
        }
        ProductPhotoDto productPhotoDto = new ProductPhotoDto(photoName);
        productPhotoDto.setProduct(product);
        return productPhotoRepository
                .save(mapStructMapper.productPhotoDtoToProductPhoto(productPhotoDto));
    }

    @Override
    public void deleteProductPhotoById(Long id) {
        try {
            ProductPhoto productPhoto = getProductById(id);
            if (productPhoto != null) {
                productPhotoRepository.deleteById(id);
            }
        } catch (ProductPhotoNotFoundException e) {
            throw new ProductPhotoNotFoundException();
        }
    }

    @Override
    public Integer getProductPhotoCountByProduct(Product product) {
        return productPhotoRepository.countProductPhotosByProduct(product);
    }
}
