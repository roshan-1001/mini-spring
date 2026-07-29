package com.roshan.minispring.beans;

import java.util.List;
import java.util.Map;

public class BeanRegistry {

    private final List<BeanDefinition> beanDefinitions;
    private final Map<Class<?>, List<BeanDefinition>> interfaceMappings;

    public BeanRegistry(List<BeanDefinition> beanDefinitions, Map<Class<?>, List<BeanDefinition>> interfaceMappings) {
        this.beanDefinitions = beanDefinitions;
        this.interfaceMappings = interfaceMappings;
    }

    public Map<Class<?>, List<BeanDefinition>> getInterfaceMappings() {
        return interfaceMappings;
    }

    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }
}
