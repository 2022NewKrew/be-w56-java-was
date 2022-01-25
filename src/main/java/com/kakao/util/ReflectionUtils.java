package com.kakao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes", "ConstantConditions", "unchecked"})
public class ReflectionUtils {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    public static Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

    public static <T> List<T> getInstancesImplementedInterface(Class<T> targetInterface) {
        String packageName = targetInterface.getPackageName();
        Set<Class> classSet = ReflectionUtils.findAllClassesUsingClassLoader(packageName);
        List<Class> targetClassList = filterImplementedClass(targetInterface, classSet);
        List<T> objectList = initiateNewInstance(targetClassList);
        return Collections.unmodifiableList(objectList);
    }

    private static <T> List<T> initiateNewInstance(List<Class> targetClassList) {
        List<T> objectList = new ArrayList<>();
        for (Class aClass : targetClassList) {
            try {
                objectList.add((T) aClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                logger.warn("Initiate controller classes error", e);
            }
        }
        return objectList;
    }

    private static <T> List<Class> filterImplementedClass(Class<T> targetInterface, Set<Class> classSet) {
        List<Class> targetClassList = new ArrayList<>();
        for (Class aClass : classSet) {
            for (Class anInterface : aClass.getInterfaces()) {
                if (anInterface.equals(targetInterface)) {
                    targetClassList.add(aClass);
                    break;
                }
            }
        }
        return targetClassList;
    }
}
