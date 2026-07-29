package com.roshan.minispring.beans;

import com.roshan.minispring.annotations.Primary;
import com.roshan.minispring.annotations.Service;
import com.roshan.minispring.exception.MiniSpringException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BeanDefinitionGenerator {

    public BeanRegistry generateBeanDefinitions(List<Class<?>> classes) {

        List<BeanDefinition> beanDefs = new ArrayList<>();
        Map<Class<?>, List<BeanDefinition>> interfaceMappings = new HashMap<>();

        for (Class<?> clazz : classes){
            Constructor<?>[] constructors;
            List<Class<?>> dependencies;
            if (clazz.isAnnotationPresent(Service.class)){

                constructors = clazz.getDeclaredConstructors();
                if(constructors.length != 1){
                    throw new MiniSpringException(clazz.getName()+"has multiple constructors, MiniSpring currently supports exactly one.");
                }

                Constructor<?> constructor;
                constructor = constructors[0];

                dependencies = List.of(constructor.getParameterTypes());

                boolean isPrimary = (clazz.isAnnotationPresent(Primary.class));

                BeanDefinition beanDef = new BeanDefinition(clazz, constructor, dependencies, isPrimary);


                beanDefs.add(beanDef);

                Class<?>[] interfaces = clazz.getInterfaces();

                for (Class<?> currInterface : interfaces){
                    interfaceMappings
                            .computeIfAbsent(currInterface, k -> new ArrayList<>())
                            .add(beanDef);
                }
            }

        }

        return new BeanRegistry(beanDefs, interfaceMappings);
    }
}
