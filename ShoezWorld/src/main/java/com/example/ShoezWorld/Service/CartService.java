package com.example.ShoezWorld.Service;
import org.springframework.stereotype.Service;

import com.example.ShoezWorld.Model.CartItem;
import com.example.ShoezWorld.Model.User;
import com.example.ShoezWorld.Repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public CartItem addCartItem(CartItem item) {
        return cartItemRepository.save(item);
    }

    public void removeCartItem(User user, Long productId) {
        cartItemRepository.deleteByUserAndProductId(user, productId);
    }

    public void clearCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(items);
    }
}