package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
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

    // - when a route includes the principal in the parameters
    // the principal will show infomration about the currently logged in user
    // - redirect attributes are for the flash message, which appears once and gone
    // once you refresh page
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            // get the current logged in user
            User user = userService.findUserByUsername(principal.getName());

            // find the product
            // .orElseThrow() -> performs a .get(); however if !.isPresent(), throws an
            // exception
            Product product = productService.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            cartItemService.addToCart(user, product, quantity);

            // d is the quantity (double), s is the item name (string)
            redirectAttributes.addFlashAttribute("message",
                    String.format("Added %d %s to your cart", quantity, product.getName()));

            return "redirect:/cart";

            // redirect user to products if exception is thrown
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("Error when adding product: %s", e.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("")
    public String viewCart(Model model, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        List<CartItem> cartItems = cartItemService.findByUser(user);
        model.addAttribute("cartItems", cartItems);

        return "cart/index";
    }

    @PostMapping("/{cartItemId}/updateQuantity")
    public String updateCartItemQuantity(@PathVariable long cartItemId, @RequestParam int newQuantity, Principal principal, RedirectAttributes redirectAttributes  ) {
     try {
         User user = userService.findUserByUsername(principal.getName());
         cartItemService.updateQuantity(cartItemId, user, newQuantity);
         redirectAttributes.addFlashAttribute("message", "Quantity updated");
         return "redirect:/cart";

       } catch (IllegalArgumentException e) {
           redirectAttributes.addFlashAttribute("error", e.getMessage());
           return "redirect:/cart";
       }
    }

    @GetMapping("/{cartItemId}/remove")
    public String removeFromCart(@PathVariable Long cartItemId,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {
     try {
       User user = userService.findUserByUsername(principal.getName());
       cartItemService.removeFromCart(cartItemId, user);
       redirectAttributes.addFlashAttribute("message", "Item removed from cart");
     } catch (IllegalArgumentException e) {
          redirectAttributes.addFlashAttribute("error", e.getMessage());
     }
       return "redirect:/cart";
    }
    
    
}