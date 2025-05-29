package com.example.ShoezWorld.Controller;

import com.example.ShoezWorld.Model.*;
import com.example.ShoezWorld.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {
    
    private final ShoeService shoeService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CartService cartService;

    @Autowired
    public MainController(ShoeService shoeService,
                        CategoryService categoryService,
                        UserService userService,
                        CartService cartService) {
        this.shoeService = shoeService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.cartService = cartService;
    }

    /* ========== HOME ENDPOINTS ========== */
    
    @GetMapping("/home/featured")
    public ResponseEntity<List<Shoe>> getFeaturedShoes() {
        return ResponseEntity.ok(shoeService.getFeaturedShoes());
    }

    @GetMapping("/home/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /* ========== PRODUCT ENDPOINTS ========== */
    
    @GetMapping("/products")
    public ResponseEntity<List<Shoe>> getAllProducts() {
        return ResponseEntity.ok(shoeService.getAllShoes());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Shoe> getProductById(@PathVariable Long id) {
        Shoe shoe = shoeService.getShoeById(id);
        return shoe != null ? ResponseEntity.ok(shoe) : ResponseEntity.notFound().build();
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<Shoe>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(shoeService.getShoesByCategory(categoryId));
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Shoe>> searchProducts(@RequestParam(required = false) String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(shoeService.getAllShoes());
        }
        return ResponseEntity.ok(shoeService.searchShoes(query));
    }

    /* ========== CART ENDPOINTS ========== */
    
    @GetMapping("/cart")
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getOrCreateCart(user));
    }

    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(
            @AuthenticationPrincipal User user,
            @RequestParam Long shoeId,
            @RequestParam(defaultValue = "1") int quantity) {
        try {
            return ResponseEntity.ok(cartService.addToCart(user, shoeId, quantity));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/cart/remove/{itemId}")
    public ResponseEntity<?> removeFromCart(
            @AuthenticationPrincipal User user,
            @PathVariable Long itemId) {
        try {
            cartService.removeFromCart(user, itemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/cart/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }

    /* ========== AUTHENTICATION ENDPOINTS ========== */
    
    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.registerUser(user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto loginDto) {
        try {
            return ResponseEntity.ok(userService.authenticate(loginDto.getUsername(), loginDto.getPassword()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* ========== USER PROFILE ENDPOINTS ========== */
    
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /* ========== ADMIN ENDPOINTS ========== */
    
    @PostMapping("/admin/categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.saveCategory(category));
    }

    @PostMapping("/admin/products")
    public ResponseEntity<Shoe> addProduct(@RequestBody Shoe shoe) {
        return ResponseEntity.ok(shoeService.saveShoe(shoe));
    }
}