package com.roshan.tests.primary;

import com.roshan.minispring.annotations.Service;

@Service
public class PaymentService {

    private final PaymentProcessor paymentProcessor;

    public PaymentService(PaymentProcessor paymentProcessor){
        this.paymentProcessor = paymentProcessor;
    }

    public PaymentProcessor getPaymentProcessor(){
        return paymentProcessor;
    }
}
