package com.roshan.demo.payment;

import com.roshan.minispring.annotations.Primary;
import com.roshan.minispring.annotations.Service;

@Primary
@Service
public class StripePaymentProcessor implements PaymentProcessor {

    public StripePaymentProcessor(){
        System.out.println("StripePaymentProcessor created");
    }

    @Override
    public void pay() {
        System.out.println("Stripe Payment");
    }
}
