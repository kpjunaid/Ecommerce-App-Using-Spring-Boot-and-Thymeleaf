package com.shoparoo.dto;

import com.shoparoo.entity.Product;
import com.shoparoo.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDto {
    private Long id;

    @NotEmpty
    private User user;

    @NotEmpty
    private Product product;

    @NotNull
    private Integer quantity;

    public CartItemDto(User user, Product product, Integer quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }
}