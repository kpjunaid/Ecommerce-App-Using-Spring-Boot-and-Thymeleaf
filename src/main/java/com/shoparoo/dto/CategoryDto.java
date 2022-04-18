package com.shoparoo.dto;

import com.shoparoo.entity.Brand;
import com.shoparoo.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
    private Long id;

    @NotEmpty
    @Size(max = 64, message = "{category.name.invalid}")
    private String name;

    @Size(max = 1024, message = "{category.description.invalid}")
    private String description;

    @Size(max = 256)
    private String photoUrl;

    private Boolean enabled;

    private Category parentCategory;

    private List<Category> subCategories = new ArrayList<>();

    private Set<Brand> brands = new HashSet<>();

    public CategoryDto(Long id, String name, String photoUrl, Boolean enabled) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.enabled = enabled;
    }

    public CategoryDto(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public CategoryDto(String name) {
        this.name = name;
    }
}