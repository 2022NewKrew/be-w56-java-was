package di;

import annotation.Inject;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.List;

public class BeanInjector {

    private final Logger logger;

    public BeanInjector(Logger logger) {
        this.logger = logger;
    }

    public void inject(BeanContainer container, Object target) {
        injectBeans(container, target);
    }

    private void injectBeans(BeanContainer container, Object target, Field field) {
        if (!field.isAnnotationPresent(Inject.class)) {
            return;
        }
        boolean accessible = field.canAccess(target);
        field.setAccessible(true);
        injectFieldValue(container, target, field);
        field.setAccessible(accessible);
    }

    private void injectFieldValue(BeanContainer container, Object target, Field field) {
        if (!field.getGenericType().getTypeName().startsWith("java.util.List")) {
            Object bean = container.getFirst(field.getType());
            injectBeans(container, bean);
            setField(target, field, bean);
            return;
        }
        String itemType = field.getGenericType()
                .getTypeName()
                .replaceAll("java.util.List<(.+)>", "$1");
        List<Object> beanList;
        try {
            beanList = container.getAll(Class.forName(itemType));
        } catch (ClassNotFoundException e) {
            logger.error("Error getting bean list for " + itemType, e);
            return;
        }
        injectBeans(container, beanList);
        setField(target, field, beanList);
    }

    private void setField(Object target, Field field, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            logger.error("Error setting field " + field.getName() + " on " + target.getClass().getName(), e);
        }
    }

    private void injectBeans(BeanContainer container, List<Object> targets) {
        for (Object target : targets) {
            injectBeans(container, target);
        }
    }

    private void injectBeans(BeanContainer container, Object target) {
        for (Field field : target.getClass().getDeclaredFields()) {
            injectBeans(container, target, field);
        }
    }
}
