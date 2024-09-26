package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/stripe/webhook")
public class StripeWebhookController {
   
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private OrderService orderServices;

    @Autowired
    public StripeWebhookController(OrderService orderServices) {
        this.orderServices = orderServices;
    }

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
        @RequestHeader("Stripe-Signature") String sigHeader ) {
            Event event;
            System.out.println("Stripe webhook called");
            try {
                event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            } catch (SignatureVerificationException e) {
                System.out.println(e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
            }
          
           
            if (event.getType().equals("checkout.session.completed")) {
                 orderServices.handleSuccessfulPayment(event);


            }
            return ResponseEntity.ok().build();
        }
}