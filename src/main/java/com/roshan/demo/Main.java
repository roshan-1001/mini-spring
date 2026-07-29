package com.roshan.demo;

import com.roshan.demo.service.OrderService;
import com.roshan.minispring.context.ApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new ApplicationContext("com.roshan.demo");

        OrderService orderService = context.getBean(OrderService.class);
        orderService.placeOrder();

        System.out.println(orderService);
    }
}
