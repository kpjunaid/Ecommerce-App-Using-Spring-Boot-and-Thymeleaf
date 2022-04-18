package com.shoparoo.dto;

import com.shoparoo.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ProductPhotoDto {
    private Long id;

    @NotEmpty
    @Size(max = 256)
    private String photoUrl;

    private Product product;

    public ProductPhotoDto(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}