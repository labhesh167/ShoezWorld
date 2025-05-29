package com.example.ShoezWorld.Repository;

import com.example.ShoezWorld.Model.Cart;
import com.example.ShoezWorld.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
    
    @Query("SELECT c FROM Cart c JOIN FETCH c.items WHERE c.user = :user")
    Cart findByUserWithItems(@Param("user") User user);
}