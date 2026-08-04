package com.roshan.tests.qualifier;

import com.roshan.minispring.annotations.Service;

@Service
public class RazorpayPaymentProcessor implements PaymentProcessor{

    @Override
    public String getName(){
        return "Razorpay";
    }
}
