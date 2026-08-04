package com.roshan.tests;

import com.roshan.minispring.annotations.Qualifier;
import com.roshan.minispring.context.ApplicationContext;
import com.roshan.tests.primary.PaymentService;
import com.roshan.tests.prototype.PrototypePrinter;
import com.roshan.tests.qualifier.QualifierPaymentService;
import com.roshan.tests.singleton.SingletonPrinter;

public class MiniSpringTests {


    public static void runAllTests() {

        testSingleton();
        testPrototype();
        testPrimary();
        testQualifier();
        testCircularDependency();
        testMissingBean();

    }

    private static void assertTrue(boolean condition, String testName){
        if(condition){
            System.out.println("✅ " + testName);
        }
        else{
            System.out.println("❌ " + testName);
        }
    }

    private static void testSingleton(){

        ApplicationContext context = new ApplicationContext("com.roshan.tests.singleton");

        SingletonPrinter p1 = context.getBean((SingletonPrinter.class));
        SingletonPrinter p2 = context.getBean((SingletonPrinter.class));

        assertTrue(p1 == p2, "Singleton Scope");
    }

    private static void testPrototype(){

        ApplicationContext context = new ApplicationContext("com.roshan.tests.prototype");

        PrototypePrinter p1 = context.getBean((PrototypePrinter.class));
        PrototypePrinter p2 = context.getBean((PrototypePrinter.class));

        assertTrue(p1 != p2, "Prototype Scope");
    }

    private static void testPrimary(){

        ApplicationContext context = new ApplicationContext("com.roshan.tests.primary");

        PaymentService paymentService = context.getBean(PaymentService.class);

        assertTrue(paymentService.getPaymentProcessor().getName().equals("Razorpay"), "@Primary");
    }

    private static void testQualifier(){

        ApplicationContext context = new ApplicationContext("com.roshan.tests.qualifier");

        QualifierPaymentService paymentService = context.getBean(QualifierPaymentService.class);

        assertTrue(paymentService.getPaymentProcessor().getName().equals("Stripe"),"@Qualifier");

    }

    private static void testMissingBean(){

        try {
            new ApplicationContext("com.roshan.tests.missingbean");
            assertTrue(false, "Missing Bean");
        } catch (Exception e){
            assertTrue(true, "Missing Bean");
        }

    }

    private static void testCircularDependency(){

        try{
            new ApplicationContext("com.roshan.tests.cirular");
            assertTrue(false, "Circular Dependency");
        } catch (Exception e){
            assertTrue(true, "Circular Dependency");
        }

    }

}
