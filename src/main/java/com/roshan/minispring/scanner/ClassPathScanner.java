package com.roshan.minispring.scanner;

import com.roshan.minispring.exception.MiniSpringException;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class ClassPathScanner {

    public List<Class<?>> scan(String basePackage){

        String packagePath = basePackage.replace('.','/');

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(packagePath);

        System.out.println(resource);

        List<Class<?>> classes =  new ArrayList<>() ;

        File file;
        try {
            file = Paths.get(resource.toURI()).toFile();
        } catch (NullPointerException | URISyntaxException e) {
            throw new MiniSpringException(basePackage + "is empty");
        }

        scanDirectory(file,basePackage,classes);

        return classes;
    }

    private void scanDirectory(File directory,String currentPackage, List<Class<?>> classes){

        if (directory == null) return;


        File[] files = directory.listFiles();

        if (files == null){
            throw
                    new MiniSpringException(currentPackage + "does not contain files");
        }

        for (File file : files ){
            if (file.isDirectory()) {
                String childPackage = currentPackage + '.' + file.getName();
                scanDirectory(file, childPackage, classes);
            }
            boolean isClassFile = file.isFile() && file.getName().endsWith(".class");

            if (isClassFile){
                String name = file.getName();
                name = currentPackage + '.' + name.substring(0, name.lastIndexOf(".class"));

                try {
                    Class<?> c = Class.forName(name);
                    classes.add(c);
                } catch (ClassNotFoundException e) {
                    throw new MiniSpringException( "Failed to load class:" + name , e);
                }
            }

        }


    }

}
