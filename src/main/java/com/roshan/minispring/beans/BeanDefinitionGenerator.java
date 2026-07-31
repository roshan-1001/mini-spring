package com.roshan.minispring.beans;

import com.roshan.minispring.annotations.Primary;
import com.roshan.minispring.annotations.Qualifier;
import com.roshan.minispring.annotations.Service;
import com.roshan.minispring.exception.MiniSpringException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BeanDefinitionGenerator {

    public BeanRegistry generateBeanDefinitions(List<Class<?>> classes) {

        List<BeanDefinition> beanDefs = new ArrayList<>();
        Map<Class<?>, List<BeanDefinition>> interfaceMappings = new HashMap<>();

        return createBeanRegistry(classes, beanDefs, interfaceMappings);
    }

    private BeanRegistry createBeanRegistry(List<Class<?>> classes, List<BeanDefinition> beanDefs, Map<Class<?>, List<BeanDefinition>> interfaceMappings){
        for (Class<?> clazz : classes){
            Constructor<?>[] constructors;
            if (clazz.isAnnotationPresent(Service.class)){

                constructors = clazz.getDeclaredConstructors();
                if(constructors.length != 1){
                    throw new MiniSpringException(clazz.getName()+"has multiple constructors, MiniSpring currently supports exactly one.");
                }

                Constructor<?> constructor = constructors[0];

                Parameter[] parameters = constructor.getParameters();

                List<ConstructorDependency> dependencies = new ArrayList<>();
                for(Parameter parameter : parameters){
                    if(parameter.isAnnotationPresent(Qualifier.class)) {
                        Qualifier qualifier = parameter.getAnnotation(Qualifier.class);
                        dependencies.add(new ConstructorDependency(parameter.getType(), qualifier.value()));
                    }else{
                        dependencies.add((new ConstructorDependency(parameter.getType())));
                    }
                }

                boolean isPrimary = (clazz.isAnnotationPresent(Primary.class));

                String simpleName = clazz.getSimpleName();
                String beanName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
                BeanDefinition beanDef = new BeanDefinition(clazz, constructor, dependencies, isPrimary, beanName);

                beanDefs.add(beanDef);

                Class<?>[] interfaces = clazz.getInterfaces();

                for (Class<?> currInterface : interfaces){
                    interfaceMappings
                            .computeIfAbsent(currInterface, k -> new ArrayList<>())
                            .add(beanDef);
                }
            }

        }

        return new BeanRegistry(beanDefs, interfaceMappings);
    }
}
