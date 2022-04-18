package com.shoparoo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "brand")
public class Brand extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false, unique = true)
    private String name;

    @Column(length = 1024)
    private String description;

    @Column(length = 256)
    private String photoUrl;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToMany
    @JoinTable(name = "brands_categories",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public Brand(Long id, String name, Boolean enabled) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
    }

    public Brand(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}