package application;

import applicationTest.annotations.RequestMapping;
import applicationTest.annotations.RequestParam;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServer;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class WebContainerTest {

    private static final Logger log = LoggerFactory.getLogger(WebServer.class);

    @Test
    void getTypesAnnotatedWith() throws ClassNotFoundException {
        String basePackage = "applicationTest";
        List<File> classFiles = getClassFiles(basePackage);
        List<Class<?>> types = getTypes(basePackage, classFiles);
        for(Class<?> clazz : types) {
            Method[] methods = clazz.getMethods();
            for(Method method : methods) {
                log.info(method.toString());
            }
        }
    }

    private List<Class<?>> getTypes(String basePackage, List<File> classFiles) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        for(File classFile : classFiles) {
            String filePath = classFile.getPath();
            String fileName = filePath.substring(filePath.indexOf(basePackage)).replace('/', '.');

            Class<?> clazz = Class.forName(fileName.substring(0, fileName.lastIndexOf(".class")));
            RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
            if(annotation != null) {
                classes.add(clazz);
            }
        }
        return classes;
    }

    @Test
    void getMethods() throws ClassNotFoundException {
        String basePackage = "applicationTest";
        List<File> classFiles = getClassFiles("applicationTest");
        List<Method> methods = getMethodAnnotatedWith(basePackage, classFiles, RequestMapping.class);

        for(Method method : methods) {
            log.info(method.getName());
            Parameter[] parameters = method.getParameters();
            for(Parameter parameter : parameters) {
                log.info("- " + parameter.getName());
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                log.info(requestParam.name());
            }
        }
    }

    private List<Method> getMethodAnnotatedWith(String basePackage, List<File> classFiles, Class<? extends Annotation> annotation) throws ClassNotFoundException {
        List<Method> methodList = new ArrayList<>();
        for(File classFile : classFiles) {
            String filePath = classFile.getPath();
            String fileName = filePath.substring(filePath.indexOf(basePackage)).replace('/', '.');
            Class<?> clazz = Class.forName(fileName.substring(0, fileName.lastIndexOf(".class")));
            Method[] methods = clazz.getMethods();
            for(Method method : methods) {
                if(method.getAnnotation(annotation) != null) {
                    methodList.add(method);
                }
            }
        }
        return methodList;
    }

    private List<File> getClassFiles(String basePackage) {
        URL resource = getClass().getClassLoader().getResource(basePackage);
        File directory = new File(resource.getFile());

        File[] files = directory.listFiles();
        Stack<File> fileStack = new Stack<>();
        fileStack.addAll(Arrays.asList(files));

        List<File> classFiles = new ArrayList<>();
        while(!fileStack.empty()) {
            File file = fileStack.pop();
            if(file.isDirectory()) {
                fileStack.addAll(Arrays.asList(file.listFiles()));
                continue;
            }

            if(file.getName().endsWith(".class")) {
                classFiles.add(file);
            }
        }
        return classFiles;
    }
}
