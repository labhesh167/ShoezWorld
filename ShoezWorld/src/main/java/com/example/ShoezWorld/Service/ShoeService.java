package com.example.ShoezWorld.Service;

import com.example.ShoezWorld.Model.Shoe;
import com.example.ShoezWorld.Repository.ShoeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShoeService {
    private final ShoeRepository shoeRepository;

    @Autowired
    public ShoeService(ShoeRepository shoeRepository) {
        this.shoeRepository = shoeRepository;
    }
    
    public List<Shoe> getAllShoes() {
        return shoeRepository.findAll();
    }
    
    public Shoe getShoeById(Long id) {
        return shoeRepository.findById(id).orElse(null);
    }
    
    public List<Shoe> getFeaturedShoes() {
        List<Shoe> allShoes = shoeRepository.findAll();
        return allShoes.subList(0, Math.min(allShoes.size(), 6));
    }
    
    public List<Shoe> getShoesByCategory(Long categoryId) {
        return shoeRepository.findByCategoryId(categoryId);
    }
    
    public List<Shoe> searchShoes(String query) {
        return shoeRepository.findByNameContainingIgnoreCase(query);
    }
    
    public Shoe saveShoe(Shoe shoe) {
        return shoeRepository.save(shoe);
    }
    
    public void deleteShoe(Long id) {
        shoeRepository.deleteById(id);
    }
}