package com.roshan.minispring.beans;

import com.roshan.minispring.annotations.Service;
import com.roshan.minispring.exception.MiniSpringException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class BeanDefinitionGenerator {

    public List<BeanDefinition> generateBeanDefinitions(List<Class<?>> classes) {

        List<BeanDefinition> beanDefs = new ArrayList<>();

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

                BeanDefinition beanDef = new BeanDefinition(clazz, constructor, dependencies);

                beanDefs.add(beanDef);
            }

        }

        return beanDefs;
    }
}
