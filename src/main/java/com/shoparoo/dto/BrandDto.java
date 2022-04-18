package com.shoparoo.dto;

import com.shoparoo.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BrandDto {
    private Long id;

    @NotEmpty
    @Size(max = 64, message = "{brand.name.invalid}")
    private String name;

    @Size(max = 1024, message = "{brand.description.invalid}")
    private String description;

    @Size(max = 256)
    private String photoUrl;

    private Boolean enabled;

    private List<Category> categories = new ArrayList<>();

    public BrandDto(String name) {
        this.name = name;
    }

    public BrandDto(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}