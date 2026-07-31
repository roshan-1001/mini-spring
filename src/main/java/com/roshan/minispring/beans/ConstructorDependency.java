package com.roshan.minispring.beans;

public class ConstructorDependency {

    private final Class<?> type;
    private final String qualifier ;

    public ConstructorDependency(Class<?> type){
        this.type = type;
        this.qualifier = null;
    }

    public ConstructorDependency(Class<?> type, String qualifier){
        this.type = type;
        this.qualifier = qualifier;
    }

    public Class<?> getType(){
        return this.type;
    }

    public String getQualifier(){
        return this.qualifier;
    }
}
