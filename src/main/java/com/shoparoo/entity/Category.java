package com.shoparoo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 256)
    private String photoUrl;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> subCategories = new ArrayList<>();

    @ManyToMany(mappedBy = "categories")
    private Set<Brand> brands = new HashSet<>();

    public Category(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public Category(Long id, String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}