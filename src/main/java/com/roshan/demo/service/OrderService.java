package com.roshan.demo.service;

import com.roshan.demo.payment.PaymentProcessor;
import com.roshan.minispring.annotations.Service;

@Service
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService){
        this.paymentService = paymentService;
        System.out.println("OrderService created");
    }

    public void placeOrder(){
        paymentService.makePayment();
    }
}
