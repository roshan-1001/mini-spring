package com.roshan.minispring.beans;

import com.roshan.minispring.enums.BeanScope;

import java.lang.reflect.Constructor;
import java.util.List;

public class BeanDefinition {

    private final Class<?> beanClass;
    private final Constructor<?> constructor;
    private final List<ConstructorDependency> dependencies;
    private final boolean primary;
    private final String beanName;
    private final BeanScope scope;

    public BeanDefinition (Class<?> beanClass, Constructor<?> constructor, List<ConstructorDependency> dependencies, boolean isPrimary, String beanName, BeanScope scope){
        this.beanClass = beanClass;
        this.constructor = constructor;
        this.dependencies = dependencies;
        this.primary = isPrimary;
        this.beanName = beanName;
        this.scope = scope;
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

    public BeanScope getScope(){ return scope;}
}
