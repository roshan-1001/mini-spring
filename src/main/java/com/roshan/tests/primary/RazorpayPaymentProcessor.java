package com.roshan.tests.primary;

import com.roshan.minispring.annotations.Primary;
import com.roshan.minispring.annotations.Service;

@Service
@Primary
public class RazorpayPaymentProcessor implements PaymentProcessor{

    @Override
    public String getName(){
        return "Razorpay";
    }
}
