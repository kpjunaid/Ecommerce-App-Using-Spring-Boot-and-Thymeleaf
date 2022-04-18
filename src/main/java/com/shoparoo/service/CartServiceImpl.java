package com.shoparoo.service;

import com.shoparoo.dto.CartItemDto;
import com.shoparoo.entity.CartItem;
import com.shoparoo.entity.Product;
import com.shoparoo.entity.User;
import com.shoparoo.exception.CartItemExistsException;
import com.shoparoo.exception.CartItemNotFoundException;
import com.shoparoo.mapper.MapStructMapper;
import com.shoparoo.repository.CartRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final MapStructMapper mapStructMapper;

    public CartServiceImpl(CartRepository cartRepository,
                           UserService userService,
                           MapStructMapper mapStructMapper) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.mapStructMapper = mapStructMapper;
    }

    @Override
    public List<CartItem> getAllCartItemsByUser(User user) {
        return cartRepository.findCartItemsByUser(user);
    }

    @Override
    public CartItem getCartItemByUserAndProduct(User user, Product product) {
        return cartRepository.findCartItemByUserAndProduct(user, product).orElseThrow(CartItemNotFoundException::new);
    }

    @Override
    @Transactional
    public void createNewCartItem(User user, Product product, Integer quantity) {
        try {
            CartItem cartItem = getCartItemByUserAndProduct(user, product);
            if (cartItem != null) {
                throw new CartItemExistsException();
            }
        } catch (CartItemNotFoundException e) {
            CartItemDto cartItemDto = new CartItemDto(user, product, quantity);
            cartRepository.save(mapStructMapper.cartItemDtoToCartItem(cartItemDto));
        }
    }

    @Override
    @Transactional
    public void changeCartItemQuantity(User user, Product product, Integer quantity) {
        try {
            CartItem cartItem = getCartItemByUserAndProduct(user, product);
            if (quantity > 0) {
                cartItem.setQuantity(quantity);
            } else {
                throw new IllegalArgumentException();
            }
            cartRepository.save(cartItem);
        } catch (CartItemNotFoundException e) {
            throw new CartItemNotFoundException();
        }
    }

    @Override
    @Transactional
    public void deleteCartItemsByUser(User user) {
        cartRepository.deleteCartItemsByUser(user);
    }

    @Override
    @Transactional
    public void deleteCartItemByUserAndProduct(User user, Product product) {
        cartRepository.deleteCartItemByUserAndProduct(user, product);
    }

    @Override
    public void addCartItemsToModel(Authentication authentication, Model model) {
        if (authentication != null) {
            User user = userService.getUserByEmail(authentication.getName());
            List<CartItem> cartItems = getAllCartItemsByUser(user);
            Double totalPrice = 0.0;
            for (CartItem cartItem: cartItems) {
                totalPrice += (cartItem.getProduct().getResalePrice() * cartItem.getQuantity());
            }
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("cartItemCount", cartItems.size());
            model.addAttribute("totalPrice", totalPrice);
        } else {
            model.addAttribute("cartItems", null);
            model.addAttribute("cartItemCount", 0);
            model.addAttribute("totalPrice", null);
        }
    }
}
