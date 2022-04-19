package com.shoparoo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 256, nullable = false)
    private String name;

    @Column(length = 1024)
    private String shortDescription;

    @Column(length = 4096)
    private String description;

    @Column(nullable = false)
    private float costPrice;

    @Column(nullable = false)
    private float resalePrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Boolean inStock;

    @Column(nullable = false)
    private Integer discount;

    @Column(nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductPhoto> productPhotos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    public Product(String name, float costPrice, float resalePrice, Integer quantity,
                   Boolean inStock, Integer discount, Boolean enabled, Category category, Brand brand) {
        this.name = name;
        this.costPrice = costPrice;
        this.resalePrice = resalePrice;
        this.quantity = quantity;
        this.inStock = inStock;
        this.discount = discount;
        this.enabled = enabled;
        this.category = category;
        this.brand = brand;
    }
}
