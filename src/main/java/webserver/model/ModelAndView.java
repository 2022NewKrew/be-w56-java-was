package webserver.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ModelAndView {
    Map<String, Object> model = new HashMap<>();
    String viewName;

    public ModelAndView() {
    }

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public void setModel(String key, Object object) {
        Map<String, Object> model = new HashMap<>();
        model.put(key, parseMap(object));

        this.model = model;
    }

    private Object parseMap(Object object) {
        try {
            List<?> objects = (ArrayList) object;

            List<Map<String, ?>> list = new ArrayList<>();
            for (Object object_ : objects)
                list.add((Map<String, ?>) parseMap(object_));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> response = new HashMap<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            String fieldName = field.getName();
            String getterName = "get" + capitalize(fieldName);
            try {
                Object value = object.getClass().getMethod(getterName).invoke(object);
                response.put(fieldName, value);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                throw new RuntimeException();
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty())
            return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public String getViewName() {
        return viewName;
    }
}
