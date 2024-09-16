package com.example.demo.controllers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ResponseBody;


// the @controller annotation tells SpringBoot that this is a controller that contains routes
// Springboot will automatically go through all the methods marked as routes and register them
@Controller
public class HomeController {

    // When the user goes to the / URL on the server, call this method
    @GetMapping("/")
    // Tell Spring Boot this method returns a HTTP response
    public String helloWorld(){
        return "home";
    }

    @GetMapping("/about-us")
    // if the route is not marked with @responsebody, then by default we are using templates
    // the 'Model model' parameter is automatically passed to aboutUs when is called by Spring
    // as the 'view model' and it also allows us to inject variables into our template
    public String aboutUs(Model model){
        // we need to return the file path to the template
        // relative to resources/templates

        // get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        //the view model is automaticaly passed to the template and
        // any attributes in it awill be available as variables
        model.addAttribute("currentDateTime", currentDateTime);

        
        return "about-us";
    }

    @GetMapping("/contact-us")
    //add @responsebody when not using html templates
    public String contactUs(){
        return "contact-us";
    }

}
