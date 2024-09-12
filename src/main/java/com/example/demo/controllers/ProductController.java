package com.example.demo.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.models.Product;
import com.example.demo.repo.ProductRepo;

@Controller
public class ProductController {
    
    @GetMapping("/products/create")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/create";
    }
    private final ProductRepo productRepo;

    @Autowired
    public ProductController(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @PostMapping("/products/create")
    public String createProduct(@ModelAttribute Product newProduct) {
        productRepo.save(newProduct);
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        List<Product> products = productRepo.findAll();
        model.addAttribute("products", products);
        return "products/index";
    }
    
    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
    Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    model.addAttribute("product", product);
    return "products/details";
    }
}

