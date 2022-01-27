package di;

import annotation.Inject;

import java.lang.reflect.Field;
import java.util.List;

public class BeanInjector {

    public void inject(BeanContainer container, Object target)
            throws ClassNotFoundException, IllegalAccessException {
        injectBeans(container, target);
    }

    private void injectBeans(BeanContainer container, Object target, Field field)
            throws ClassNotFoundException, IllegalAccessException {
        if (!field.isAnnotationPresent(Inject.class)) {
            return;
        }
        boolean accessible = field.canAccess(target);
        field.setAccessible(true);
        injectFieldValue(container, target, field);
        field.setAccessible(accessible);
    }

    private void injectFieldValue(BeanContainer container, Object target, Field field)
            throws ClassNotFoundException, IllegalAccessException {
        if (field.getGenericType().getTypeName().startsWith("java.util.List")) {
            String itemType = field.getGenericType()
                    .getTypeName()
                    .replaceAll("java.util.List<(.+)>", "$1");
            List<Object> beanList = container.getAll(Class.forName(itemType));
            injectBeans(container, beanList);
            field.set(target, beanList);
        } else {
            Object bean = container.getFirst(field.getType());
            injectBeans(container, bean);
            field.set(target, bean);
        }
    }

    private void injectBeans(BeanContainer container, List<Object> targets)
            throws ClassNotFoundException, IllegalAccessException {
        for (Object target : targets) {
            injectBeans(container, target);
        }
    }

    private void injectBeans(BeanContainer container, Object target)
            throws ClassNotFoundException, IllegalAccessException {
        for (Field field : target.getClass().getDeclaredFields()) {
            injectBeans(container, target, field);
        }
    }
}
