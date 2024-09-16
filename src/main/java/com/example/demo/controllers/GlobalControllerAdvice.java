package com.example.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


// a controller advice is applied to all other controllers
// you can put filtering rules to state for a particular advice, which url it appears
@ControllerAdvice
public class GlobalControllerAdvice {

    // this advice is applied whenever the templates refer to the 'currentUser' variable
   @ModelAttribute("currentUser")
   public String getCurrentUser() {
        // get the current authentication context (i.e, the current detials of the logged in user)
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

       // if no user is logged in, return null
       if (authentication == null || !authentication.isAuthenticated()) {
           return null;
       }
       // return the name of the currently logged in user
       return authentication.getName();
   }

   @ModelAttribute("isAuthenticated")
   public boolean isAuthenticated() {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       if (authentication.getName().equals("anonymousUser")){
        return false;
       }
       return true;
   }
}

