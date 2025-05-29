package com.example.ShoezWorld.Repository;

import com.example.ShoezWorld.Model.Shoe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShoeRepository extends JpaRepository<Shoe, Long> {
    List<Shoe> findByCategoryId(Long categoryId);
    List<Shoe> findByNameContainingIgnoreCase(String name);
    List<Shoe> findByBrandContainingIgnoreCase(String brand);
}