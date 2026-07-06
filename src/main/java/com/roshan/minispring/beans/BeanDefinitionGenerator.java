package com.roshan.minispring.beans;

import com.roshan.minispring.annotations.Service;

import java.util.ArrayList;
import java.util.List;

public class BeanDefinitionGenerator {

    public List<BeanDefinition> generateBeanDefinitions(List<Class<?>> classes) {

        List<BeanDefinition> beanDefs = new ArrayList<>();

        for (Class<?> clazz : classes){

            if (clazz.isAnnotationPresent(Service.class)){
                BeanDefinition beanDef = new BeanDefinition(clazz);
                beanDefs.add(beanDef);
            }

        }

        return beanDefs;
    }
}
