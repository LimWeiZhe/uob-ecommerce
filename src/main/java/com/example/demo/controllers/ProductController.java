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
    
    // dependency injection = when spring boot creates an instance of ProductController, 
    //it will automatically create an instance of ProductRepo 
    //and pass it to the new instance of ProductController
    @Autowired
    public ProductController(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    private final ProductRepo productRepo;


    // when we want to add forms, we always need 2 routes
    // 1 route to display the form
    // 1 route to process the form
    @GetMapping("/products/create")
    public String showCreateProductForm(Model model) {
        //send an empty instance of the product model to the template
        model.addAttribute("product", new Product());
        return "products/create";
    }

    // creates a new product based on the inputs in create.hmtl
    // and saves into the repo
    @PostMapping("/products/create")
    public String createProduct(@ModelAttribute Product newProduct) {
        productRepo.save(newProduct);

        //a redirect tells the client to go to a different URL
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
    
        // find the product with the matching id
        Product product = productRepo.findById(id)
                // throw an exception if product is not found
                .orElseThrow(() -> new RuntimeException("Product not found"));
        // add it  the view model            
        model.addAttribute("product", product);
        return "products/details";
    }

    
    @GetMapping("/products/{id}/edit")
    public String showUpdateProduct(@PathVariable Long id, Model model) {
       Product product = productRepo.findById(id).orElseThrow( () -> new RuntimeException("Product not found"));
       model.addAttribute("product", product);
       return "products/edit"; 
   }
   
   @PostMapping("/products/{id}/edit")
   public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
     product.setId(id); // Ensure we're updating the correct product
     productRepo.save(product);
     return "redirect:/products";
   }
  
  
}

