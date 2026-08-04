package com.roshan.tests.qualifier;

import com.roshan.minispring.annotations.Qualifier;
import com.roshan.minispring.annotations.Service;

@Service
public class QualifierPaymentService {

    private final PaymentProcessor paymentProcessor;

    public QualifierPaymentService(@Qualifier("stripePaymentProcessor") PaymentProcessor paymentProcessor){
        this.paymentProcessor = paymentProcessor;
    }

    public PaymentProcessor getPaymentProcessor() {
        return paymentProcessor;
    }
}
