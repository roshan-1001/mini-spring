package com.roshan.minispring.beans;

import java.lang.reflect.Constructor;
import java.util.List;

public class BeanDefinition {

    private final Class<?> beanClass;
    private final Constructor<?> constructor;
    private final List<ConstructorDependency> dependencies;
    private final boolean primary;
    private final String beanName;

    public BeanDefinition (Class<?> beanClass, Constructor<?> constructor, List<ConstructorDependency> dependencies, boolean isPrimary, String beanName){
        this.beanClass = beanClass;
        this.constructor = constructor;
        this.dependencies = dependencies;
        this.primary = isPrimary;
        this.beanName = beanName;
    }

    public Class<?> getBeanClass(){
        return beanClass;
    }

    public Constructor<?> getConstructor(){
        return constructor;
    }

    public List<ConstructorDependency> getDependencies(){
        return dependencies;
    }

    public boolean isPrimary(){
        return primary;
    }

    public String getBeanName(){ return this.beanName;}
}
