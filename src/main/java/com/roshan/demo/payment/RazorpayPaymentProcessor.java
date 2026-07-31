package com.roshan.demo.payment;

import com.roshan.minispring.annotations.Primary;
import com.roshan.minispring.annotations.Service;


@Service
public class RazorpayPaymentProcessor implements PaymentProcessor{

    public RazorpayPaymentProcessor() {
        System.out.println("RazorpayPaymentProcessor created");
    }

    @Override
    public void pay() {
        System.out.println("Razorpay Payment");
    }
}
