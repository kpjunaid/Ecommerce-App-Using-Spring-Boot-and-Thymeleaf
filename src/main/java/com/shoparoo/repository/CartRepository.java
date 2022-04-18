package com.shoparoo.repository;

import com.shoparoo.entity.CartItem;
import com.shoparoo.entity.Product;
import com.shoparoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findCartItemsByUser(User user);
    Optional<CartItem> findCartItemByUserAndProduct(User user, Product product);
    void deleteCartItemsByUser(User user);
    void deleteCartItemByUserAndProduct(User user, Product product);
}
