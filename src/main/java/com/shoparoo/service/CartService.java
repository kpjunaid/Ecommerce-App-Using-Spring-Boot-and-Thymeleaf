package com.shoparoo.service;

import com.shoparoo.entity.CartItem;
import com.shoparoo.entity.Product;
import com.shoparoo.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.List;

public interface CartService {
    List<CartItem> getAllCartItemsByUser(User user);
    CartItem getCartItemByUserAndProduct(User user, Product product);
    void createNewCartItem(User user, Product product, Integer quantity);
    void changeCartItemQuantity(User user, Product product, Integer quantity);
    void deleteCartItemsByUser(User user);
    void deleteCartItemByUserAndProduct(User user, Product product);
    void addCartItemsToModel(Authentication authentication, Model model);
}
