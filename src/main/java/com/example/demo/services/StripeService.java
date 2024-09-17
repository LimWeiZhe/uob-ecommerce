package com.example.demo.services;

import com.example.demo.models.CartItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class StripeService {

    @Value("${stripe.api.secretKey}")
    private String stripeSecretKey;

    @Value("${stripe.api.publicKey}")
    private String stripePublicKey;



    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public Session createCheckoutSession(List<CartItem> cartItems, String successUrl, String cancelUrl) throws StripeException {
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        // 1. create line items
        // a line item is the description of the product + quantity _ price per unit (i.e one line in an invoice)
        // 2. pass all the line items, along with the payment requirements (like what currecny) to Stripe
        // 3. receive the checkout session id from Strip
        for (CartItem item : cartItems) {
            SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
                    .builder()
                    .setName(item.getProduct().getName()) // set the name of the product
                    .putMetadata("product_id", item.getProduct().getId().toString()) 
                    .build();

            // price data consists of unit amount, currency and product information (which we have created as "product data")
            SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData
                    .builder()
                    .setCurrency("usd")
                    .setUnitAmount(item.getProduct().getPrice().multiply(new BigDecimal("100")).longValue()) // default is in cents
                    .setProductData(productData)
                    .build();
            // line items consists of quantity and price data
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem
                    .builder()
                    .setPriceData(priceData)
                    .setQuantity((long) item.getQuantity())
                    .build();

            lineItems.add(lineItem);
        }

        // create the checkout session
        SessionCreateParams params = SessionCreateParams
                .builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(cancelUrl)
                .setSuccessUrl(successUrl)
                .addAllLineItem(lineItems)
                .build();

        return Session.create(params);
    }

    public String getPublicKey() {
        return stripePublicKey;
    }
   
}