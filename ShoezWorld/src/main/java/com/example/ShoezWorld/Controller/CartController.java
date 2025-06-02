package com.example.ShoezWorld.Controller;

import com.example.ShoezWorld.Model.CartItem;
import com.example.ShoezWorld.Model.Product;
import com.example.ShoezWorld.Model.User;
import com.example.ShoezWorld.Service.CartService;
import com.example.ShoezWorld.Service.ProductService;
import com.example.ShoezWorld.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @GetMapping("/{username}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        List<CartItem> items = cartService.getCartItems(userOpt.get());
        return ResponseEntity.ok(items);
    }

    @PostMapping("/{username}/add")
    public ResponseEntity<?> addCartItem(@PathVariable String username, @RequestBody CartItem cartItem) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty())
            return ResponseEntity.badRequest().body("User not found");

        Optional<Product> productOpt = productService.findById(cartItem.getProduct().getId());
        if (productOpt.isEmpty())
            return ResponseEntity.badRequest().body("Product not found");

        cartItem.setUser(userOpt.get());
        cartItem.setProduct(productOpt.get());

        CartItem savedItem = cartService.addCartItem(cartItem);
        return ResponseEntity.ok(savedItem);
    }

    @DeleteMapping("/{username}/remove/{productId}")
    public ResponseEntity<?> removeCartItem(@PathVariable String username, @PathVariable Long productId) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty())
            return ResponseEntity.badRequest().body("User not found");

        cartService.removeCartItem(userOpt.get(), productId);
        return ResponseEntity.ok("Item removed");
    }

    @DeleteMapping("/{username}/clear")
    public ResponseEntity<?> clearCart(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty())
            return ResponseEntity.badRequest().body("User not found");

        cartService.clearCart(userOpt.get());
        return ResponseEntity.ok("Cart cleared");
    }
}