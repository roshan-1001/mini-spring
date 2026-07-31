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

        List<ConstructorDependency> constructorDependencies = beanDefinition.getDependencies();

        List<BeanDefinition> resolvedDependencies = new ArrayList<>();

        for(ConstructorDependency dependency: constructorDependencies){
            BeanDefinition bd = beanDefinitions.get(dependency.getType());
            if(bd==null){
                bd = resolveBeanDefinition(dependency, clazz);
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

    private BeanDefinition resolveBeanDefinition(ConstructorDependency dependency, Class<?> clazz){

        BeanDefinition bd = null;
        if(!interfaceMappings.containsKey(dependency.getType())){
            throw new MiniSpringException("Dependency " + dependency.getType().getName() + "not found for " + clazz.getName());
        }
        else if(interfaceMappings.get(dependency.getType()).size()==1){
            bd = interfaceMappings.get(dependency.getType()).getFirst();
        }
        else if(interfaceMappings.get(dependency.getType()).size()>1) {

            List<BeanDefinition> implementations = interfaceMappings.get(dependency.getType());


            if(dependency.getQualifier() != null){

                for(BeanDefinition implementation: implementations){
                    if(implementation.getBeanName().equals(dependency.getQualifier())){
                        bd = implementation;
                    }
                }
                if(bd == null) {
                    throw new MiniSpringException("The qualifier: " + dependency.getQualifier() + "does not exist");
                }
                return bd;

            }

            int primaryCount = 0;
            for (BeanDefinition beanDef : implementations) {
                if (beanDef.isPrimary()) {
                    bd = beanDef;
                    primaryCount++;
                }
                if (primaryCount > 1) {
                    throw new MiniSpringException("Interface " + dependency.getType().getName() + " has more than one 'Primary' implementations");
                }
            }

        }
        if (bd == null){
            throw new MiniSpringException("Could not find qualifier or a primary implementation for interface: " + dependency.getType().getName());
        }
        return bd;
    }


}
