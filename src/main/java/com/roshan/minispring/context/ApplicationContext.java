package com.roshan.minispring.context;

import com.roshan.minispring.beans.BeanDefinitionGenerator;
import com.roshan.minispring.beans.BeanFactory;
import com.roshan.minispring.beans.BeanRegistry;
import com.roshan.minispring.exception.MiniSpringException;
import com.roshan.minispring.scanner.ClassPathScanner;

import java.util.List;

public class ApplicationContext {

    private final BeanFactory beanFactory;

    public ApplicationContext(String basePackage){

        ClassPathScanner scanner = new ClassPathScanner();
        List<Class<?>> classes = scanner.scan(basePackage);

        BeanDefinitionGenerator beanDefinitionGenerator = new BeanDefinitionGenerator();
        BeanRegistry beanRegistry = beanDefinitionGenerator.generateBeanDefinitions(classes);

        this.beanFactory  = new BeanFactory(beanRegistry);

    }

    public <T> T getBean(Class<T> clazz){

        T bean = beanFactory.getBean(clazz);

        if( bean == null){
            throw new MiniSpringException("No bean found for the type: " + clazz.getName());
        }
        return bean;
    }
}
