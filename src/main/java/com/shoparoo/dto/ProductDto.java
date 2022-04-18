package com.shoparoo.dto;

import com.shoparoo.entity.Brand;
import com.shoparoo.entity.Category;
import com.shoparoo.entity.ProductPhoto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private Long id;

    @NotEmpty
    @Size(max = 256, message = "{product.name.invalid}")
    private String name;

    @Size(max = 1024, message = "{product.shortDescription.invalid}")
    private String shortDescription;

    @Size(max = 4096, message = "{product.description.invalid}")
    private String description;

    @NotNull(message = "{product.costPrice.invalid}")
    private float costPrice;

    @NotNull(message = "{product.resalePrice.invalid}")
    private float resalePrice;

    @NotNull(message = "{product.quantity.invalid}")
    private Integer quantity;

    @NotNull(message = "{product.discount.invalid}")
    private Integer discount;

    @NotNull(message = "{product.inStock.invalid}")
    private Boolean inStock;

    @NotNull
    private Boolean enabled;

    private List<ProductPhoto> productPhotos = new ArrayList<>();

    @NotNull(message = "{product.category.invalid}")
    private Category category;

    @NotNull(message = "{product.brand.invalid}")
    private Brand brand;
}