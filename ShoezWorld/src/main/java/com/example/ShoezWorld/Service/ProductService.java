package com.example.ShoezWorld.Service;
import com.example.ShoezWorld.Model.Product;
import com.example.ShoezWorld.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> getByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}