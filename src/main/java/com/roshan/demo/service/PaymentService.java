package com.roshan.demo.service;


import com.roshan.demo.payment.PaymentProcessor;
import com.roshan.minispring.annotations.Service;

@Service
public class PaymentService {

    private final PaymentProcessor paymentProcessor;

    public PaymentService(PaymentProcessor paymentProcessor){

        this.paymentProcessor = paymentProcessor;
        System.out.println("PaymentService created");
    }

    public void makePayment() {
        paymentProcessor.pay();
        System.out.println("Payment Completed");
    }
}
