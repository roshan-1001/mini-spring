package com.roshan.minispring.beans;

import java.lang.reflect.Constructor;
import java.util.List;

public class BeanDefinition {

    private final Class<?> beanClass;
    private final Constructor<?> constructor;
    private final List<Class<?>> dependencies;

    private final boolean primary;

    public BeanDefinition (Class<?> beanClass, Constructor<?> constructor, List<Class<?>> dependencies, boolean isPrimary){
        this.beanClass = beanClass;
        this.constructor = constructor;
        this.dependencies = dependencies;
        this.primary = isPrimary;
    }

    public Class<?> getBeanClass(){
        return beanClass;
    }

    public Constructor<?> getConstructor(){
        return constructor;
    }

    public List<Class<?>> getDependencies(){
        return dependencies;
    }

    public boolean isPrimary(){
        return primary;
    }
}
