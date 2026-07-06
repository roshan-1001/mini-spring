package com.roshan.minispring.beans;

import com.roshan.minispring.exception.MiniSpringException;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BeanFactory {

    public Map<Class<?>, Object> createBeans(List<BeanDefinition> beanDefinitions){

        Map<Class<?>, Object> beans = new HashMap<>();

        for (BeanDefinition beanDef : beanDefinitions ){

            Class<?> clazz = beanDef.getBeanClass();
            Constructor<?> constructor;
            try {
                constructor = clazz.getDeclaredConstructor();
                Object bean = constructor.newInstance();
                beans.put(clazz,bean);
            } catch (ReflectiveOperationException e) {
                throw new MiniSpringException("Failed to create bean: " + clazz.getName(),e);
            }



        }

        return beans;
    }
}
