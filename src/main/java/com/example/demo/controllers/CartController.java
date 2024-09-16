package com.example.demo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.services.CartItemService;
import com.example.demo.services.ProductService;
import com.example.demo.services.UserService;


@Controller
@RequestMapping("/cart")
public class CartController {
   private final CartItemService cartItemService;
   private final ProductService productService;
   private final UserService userService;

   @Autowired
   public CartController(CartItemService cartItemService, ProductService productService, UserService userService) {
       this.cartItemService = cartItemService;
       this.productService = productService;
       this.userService = userService;
   }
}