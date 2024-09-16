package com.example.demo.repo;

import com.example.demo.models.Product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// interface between us and the database

public interface ProductRepo extends JpaRepository<Product, Long> {
    // for eager? loading, one big query instead of multiple queries
    // JPQL instead of SQL, translate this into SQL
   // @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category")
   // List<Product> findAllWithCategories();
    // this version allows us to fetch the tags easier
   @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.category " +
           "LEFT JOIN FETCH p.tags")
   List<Product> findAllWithCategoriesAndTags();

}