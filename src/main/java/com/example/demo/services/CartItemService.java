package com.example.demo.services;

import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.repo.CartItemRepo;

import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repo.CartItemRepo;

@Service
public class CartItemService {
    // reference to cart item repository
    private final CartItemRepo cartItemRepo;

    @Autowired
    public CartItemService(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    // when a method is transactional, if throws an exception for any reason,
    // all database writes and updates will be undone
    @Transactional
    public CartItem addToCart(User user, Product product, int quantity) {
        // does the item already exist in the cart?
        // var <CartItem>
        Optional<CartItem> existingItem = cartItemRepo.findByUserAndProduct(user, product);

        // if the product already exists in the cart
        // .isPresetnt() checks if there is a value or not
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            return cartItemRepo.save(cartItem);

            // if the product is not in the cart
        } else {
            CartItem newItem = new CartItem(user, product, quantity);
            return cartItemRepo.save(newItem);
        }
    }

    public List<CartItem> findByUser(User user) {
        // todo: for further business logic
        // ex: recommendations, discount code, out of stock notice, price change
        return cartItemRepo.findByUser(user);

    }

    @Transactional
    public void updateQuantity(long cartItemId, User user, int newQuantity) {
        CartItem existingItem = cartItemRepo.findByUserAndId(user, cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("No cart item with this id"));
        existingItem.setQuantity(newQuantity);
        cartItemRepo.save(existingItem);
    }

    @Transactional
    public void removeFromCart(long cartItemId, User user) {
       cartItemRepo.deleteByIdAndUser(cartItemId, user);
    }
}
