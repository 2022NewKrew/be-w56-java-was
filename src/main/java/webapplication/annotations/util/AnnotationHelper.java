package webapplication.annotations.util;

import webserver.WebServer;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class AnnotationHelper {

    public static List<Method> getMethodsAnnotatedWith(String basePackage, Class<? extends Annotation> annotation) throws ClassNotFoundException {
        List<File> classFiles = getClassFiles(basePackage);
        List<Method> methodList = new ArrayList<>();
        for(File classFile : classFiles) {
            Class<?> clazz = Class.forName(getClassNameFromFile(basePackage, classFile));
            Method[] methods = clazz.getMethods();
            for(Method method : methods) {
                if(method.getAnnotation(annotation) != null) {
                    methodList.add(method);
                }
            }
        }
        return methodList;
    }

    private static List<File> getClassFiles(String basePackage) {
        URL resource = WebServer.class.getClassLoader().getResource(basePackage);
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

    private static String getClassNameFromFile(String basePackage, File file) {
        String filePath = file.getPath();
        String fileName = filePath.substring(filePath.indexOf(basePackage)).replace('/', '.');
        return fileName.substring(0, fileName.lastIndexOf(".class"));
    }

}
