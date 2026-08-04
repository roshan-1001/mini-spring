package com.roshan.minispring.beans;

import com.roshan.minispring.enums.BeanScope;
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
        createBeans();
    }

    public void createBeans(){

        for (BeanDefinition bd : this.beanDefinitions.values()){
            if(bd.getScope() == BeanScope.PROTOTYPE){
                continue;
            }
            createBean(bd);
        }

    }

    public <T> T getBean(Class<T> clazz){

        BeanDefinition bd = beanDefinitions.get(clazz);

        if(bd == null){
            throw new MiniSpringException("No bean registered for type: " +  clazz.getName());
        }

        if(bd.getScope() == BeanScope.SINGLETON){
            return clazz.cast(beans.get(clazz));
        }
        else if(bd.getScope() == BeanScope.PROTOTYPE){
            return clazz.cast(createBean(bd));
        }
        else{
            throw new MiniSpringException("Working with only 2 scopes for now");
        }
    }

    private Object createBean(BeanDefinition beanDefinition){

        Class<?> clazz = beanDefinition.getBeanClass();

        //check singleton cache
        if (beanDefinition.getScope() == BeanScope.SINGLETON && this.beans.containsKey(clazz)){
            return this.beans.get(clazz);
        }

        //detect circular dependency
        if (beansInCreation.contains(clazz)){
            throw new MiniSpringException("Circular dependency detected for class: " + clazz.getName());
        }

        //add the current being that is processed to beansInCreation
        this.beansInCreation.add(clazz);

        List<ConstructorDependency> constructorDependencies = beanDefinition.getDependencies();
        List<Object> dependencyObjects = new ArrayList<>();

        for(ConstructorDependency dependency: constructorDependencies){
            BeanDefinition bd = beanDefinitions.get(dependency.getType());
            if(bd==null){
                bd = resolveDependency(dependency, clazz);
            }
            dependencyObjects.add(createBean(bd));
        }

        Constructor<?> constructor = beanDefinition.getConstructor();
        Object createdBean;
        try {
            createdBean = constructor.newInstance(dependencyObjects.toArray());
        } catch (Exception e){
            throw new MiniSpringException("Cannot construct " + clazz.getName(),e);
        }finally {
            this.beansInCreation.remove(clazz);
        }

        if(beanDefinition.getScope() == BeanScope.SINGLETON) {
            this.beans.put(clazz, createdBean);
        }
        return createdBean;

    }

    private BeanDefinition resolveDependency(ConstructorDependency dependency, Class<?> clazz){

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
