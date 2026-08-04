package com.roshan.tests.testMissingBean;


import com.roshan.minispring.annotations.Service;

@Service
public class PaymentService {

    public PaymentService(PaymentProcessor paymentProcessor){

    }

}
