package com.example.ShoezWorld.Service;
import com.example.ShoezWorld.Model.Order;
import com.example.ShoezWorld.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ShoezWorld.Model.User;
import java.util.List;
import java.time.LocalDateTime;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order placeOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}