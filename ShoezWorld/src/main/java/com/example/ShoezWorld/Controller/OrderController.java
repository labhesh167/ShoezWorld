package com.example.ShoezWorld.Controller;


import com.example.ShoezWorld.Model.Order;
import com.example.ShoezWorld.Model.User;
import com.example.ShoezWorld.Service.OrderService;
import com.example.ShoezWorld.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/{username}/place")
    public ResponseEntity<?> placeOrder(@PathVariable String username, @RequestBody Order order) {
        Optional<User> userOpt = userService.findByUsername(username);
        if(userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");

        order.setUser(userOpt.get());
        Order placedOrder = orderService.placeOrder(order);
        return ResponseEntity.ok(placedOrder);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if(userOpt.isEmpty()) return ResponseEntity.badRequest().build();

        List<Order> orders = orderService.getOrdersByUser(userOpt.get());
        return ResponseEntity.ok(orders);
    }
}
