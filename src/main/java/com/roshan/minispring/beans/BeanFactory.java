package com.roshan.minispring.beans;

import com.roshan.minispring.exception.MiniSpringException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanFactory {

    private final Map<Class<?>, BeanDefinition> beanDefinitions = new HashMap<>();
    private final Map<Class<?>, Object> beans = new HashMap<>();

    private Object createBean( BeanDefinition beanDefinition){
        Class<?> clazz = beanDefinition.getBeanClass();

        if (this.beans.containsKey(clazz)){
            return this.beans.get(clazz);
        }

        List<Class<?>> dependencies = beanDefinition.getDependencies();

        for(Class<?> dependency: dependencies){
            BeanDefinition bd = beanDefinitions.get(dependency);
            if(bd == null){
                throw new MiniSpringException("Dependency " + dependency.getName() + "not found for " + clazz.getName());
            }
            createBean(bd);
        }

        Constructor<?> constructor = beanDefinition.getConstructor();

        List<Object> dependencyObjects = new ArrayList<>();
        for(Class<?> dependency: dependencies){
            dependencyObjects.add(beans.get(dependency));
        }

        Object createdBean;
        try {
            createdBean = constructor.newInstance(dependencyObjects.toArray());
        } catch (Exception e){
            throw new MiniSpringException("Cannot construct " + clazz.getName(),e);
        }

        this.beans.put(clazz, createdBean);

        return createdBean;

    }



    public Map<Class<?>, Object> createBeans(List<BeanDefinition> beanDefinitions){

        for (BeanDefinition bd : beanDefinitions){
            this.beanDefinitions.put(bd.getBeanClass(),bd);
        }

        for(BeanDefinition bd : beanDefinitions){
            createBean(bd);
        }

        return this.beans;
    }
}
