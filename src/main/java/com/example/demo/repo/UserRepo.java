package com.example.demo.repo;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    // automated query generation, jpa is able to infer the method 
    // will convert baed on the method name to:
    // SELECT * from user_accounts WHERE username =:?
   User findByUsername(String username);
}