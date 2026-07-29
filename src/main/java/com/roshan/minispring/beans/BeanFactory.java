package com.roshan.minispring.beans;

import com.roshan.minispring.exception.MiniSpringException;

import java.lang.reflect.Constructor;
import java.util.*;

public class BeanFactory {

    private final Map<Class<?>, BeanDefinition> beanDefinitions =  new HashMap<>();
    private final Map<Class<?>, List<BeanDefinition>> interfaceMappings = new HashMap<>();

    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final Set<Class<?>> beansInCreation = new HashSet<>();

    public BeanFactory (BeanRegistry beanRegistry){

        for(BeanDefinition bd : beanRegistry.getBeanDefinitions()){
            this.beanDefinitions.put(bd.getBeanClass(),bd);
        }
        this.interfaceMappings.putAll(beanRegistry.getInterfaceMappings());
    }

    public Map<Class<?>, Object> createBeans(){

        for (BeanDefinition bd : this.beanDefinitions.values()){
            createBean(bd);
        }
        return this.beans;
    }

    private void createBean(BeanDefinition beanDefinition){
        Class<?> clazz = beanDefinition.getBeanClass();

        if (this.beans.containsKey(clazz)){
            return;
        }

        if (beansInCreation.contains(clazz)){
            throw new MiniSpringException("Circular dependency detected for class: " + clazz.getName());
        }

        this.beansInCreation.add(clazz);

        List<Class<?>> dependencies = beanDefinition.getDependencies();
        List<BeanDefinition> resolvedDependencies = new ArrayList<>();

        for(Class<?> dependency: dependencies){
            BeanDefinition bd = beanDefinitions.get(dependency);
            if(bd==null){
                if(!interfaceMappings.containsKey(dependency)){
                    throw new MiniSpringException("Dependency " + dependency.getName() + "not found for " + clazz.getName());
                }
                else if(interfaceMappings.get(dependency).size()==1){
                    bd = interfaceMappings.get(dependency).getFirst();
                }
                else if(interfaceMappings.get(dependency).size()>1) {
                    List<BeanDefinition> implementations = interfaceMappings.get(dependency);
                    int primaryCount = 0;
                    for (BeanDefinition beanDef : implementations) {
                        if (beanDef.isPrimary()) {
                            bd = beanDef;
                            primaryCount++;
                        }
                        if (primaryCount>1){
                            throw new MiniSpringException("Interface " + dependency.getName() + " has more than one 'Primary' implementations");
                        }
                    }
                }
                if (bd == null){
                    throw new MiniSpringException("Could not find primary implementation for interface: " + dependency.getName());
                }
            }
            createBean(bd);
            resolvedDependencies.add(bd);
        }

        Constructor<?> constructor = beanDefinition.getConstructor();

        List<Object> dependencyObjects = new ArrayList<>();
        for(BeanDefinition bd: resolvedDependencies){
            dependencyObjects.add(beans.get(bd.getBeanClass()));
        }

        Object createdBean;
        try {
            createdBean = constructor.newInstance(dependencyObjects.toArray());
        } catch (Exception e){
            throw new MiniSpringException("Cannot construct " + clazz.getName(),e);
        }finally {
            this.beansInCreation.remove(clazz);
        }

        this.beans.put(clazz, createdBean);

    }


}
