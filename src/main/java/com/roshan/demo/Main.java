package com.roshan.demo;

import com.roshan.demo.service.OrderService;
import com.roshan.demo.service.PaymentService;
import com.roshan.minispring.context.ApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new ApplicationContext("com.roshan.demo");

        PaymentService paymentService = context.getBean(PaymentService.class);
        OrderService orderService = context.getBean(OrderService.class);

        System.out.println(paymentService);
        System.out.println(orderService);
    }
}
