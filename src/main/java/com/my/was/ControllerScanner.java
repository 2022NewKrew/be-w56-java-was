package com.my.was;

import com.my.was.container.handlermappings.annotation.Controller;
import org.reflections.Reflections;

import java.util.Set;

public class ControllerScanner {

    public Set<Class<?>> scan() {
        Reflections reflections = new Reflections(getClass().getPackageName());
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

}
