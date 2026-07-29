package com.roshan.minispring.context;

import com.roshan.minispring.beans.BeanDefinition;
import com.roshan.minispring.beans.BeanDefinitionGenerator;
import com.roshan.minispring.beans.BeanFactory;
import com.roshan.minispring.beans.BeanRegistry;
import com.roshan.minispring.exception.MiniSpringException;
import com.roshan.minispring.scanner.ClassPathScanner;

import java.util.List;
import java.util.Map;

public class ApplicationContext {

    private final Map<Class<?>, Object> beans;

    public ApplicationContext(String basePackage){

        ClassPathScanner scanner = new ClassPathScanner();
        List<Class<?>> classes = scanner.scan(basePackage);

        BeanDefinitionGenerator beanDefinitionGenerator = new BeanDefinitionGenerator();
        BeanRegistry beanRegistry = beanDefinitionGenerator.generateBeanDefinitions(classes);

        BeanFactory beanFactory  = new BeanFactory(beanRegistry);
        beans = beanFactory.createBeans();

    }

    public <T> T getBean(Class<T> clazz){

        if( beans.get(clazz) == null){
            throw new MiniSpringException("No bean found for the type: " + clazz.getName());
        }
        return  clazz.cast(beans.get(clazz));
    }
}
