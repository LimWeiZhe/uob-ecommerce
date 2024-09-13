package com.example.demo.repo;

import com.example.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
// interface between us and the database

public interface ProductRepo extends JpaRepository<Product, Long> {
}