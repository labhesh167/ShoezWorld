package com.example.ShoezWorld.Service;

import com.example.ShoezWorld.Model.*;
import com.example.ShoezWorld.Repository.CartRepository;
import com.example.ShoezWorld.Repository.ShoeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final ShoeRepository shoeRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ShoeRepository shoeRepository) {
        this.cartRepository = cartRepository;
        this.shoeRepository = shoeRepository;
    }

    public Cart getOrCreateCart(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    public Cart addToCart(User user, Long shoeId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        // 1. Get or create cart
        Cart cart = getOrCreateCart(user);
        
        // 2. Find the shoe
        Shoe shoe = shoeRepository.findById(shoeId)
            .orElseThrow(() -> new RuntimeException("Shoe not found with id: " + shoeId));
        
        // 3. Check stock availability
        if (shoe.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available. Only " + shoe.getStockQuantity() + " left.");
        }
        
        // 4. Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
            .filter(item -> item.getShoe().getId().equals(shoeId))
            .findFirst();
        
        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Add new item
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setShoe(shoe);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }
        
        // 5. Recalculate total
        cart.calculateTotal();
        
        return cartRepository.save(cart);
    }

    public void removeFromCart(User user, Long cartItemId) {
        Cart cart = getOrCreateCart(user);
        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        
        if (!removed) {
            throw new RuntimeException("Cart item not found with id: " + cartItemId);
        }
        
        cart.calculateTotal();
        cartRepository.save(cart);
    }

    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cart.setTotalPrice(0);
        cartRepository.save(cart);
    }
}