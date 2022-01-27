package di;

import annotation.Inject;

import java.lang.reflect.Field;
import java.util.List;

public class BeanInjector {

    private final BeanContainer container;

    public BeanInjector(BeanContainer container) {
        this.container = container;
    }

    public void inject(Object target)
            throws ClassNotFoundException, IllegalAccessException {
        injectBeans(target);
    }

    private void injectBeans(Object target, Field field)
            throws ClassNotFoundException, IllegalAccessException {
        if (!field.isAnnotationPresent(Inject.class)) {
            return;
        }
        boolean accessible = field.canAccess(target);
        field.setAccessible(true);
        injectFieldValue(target, field);
        field.setAccessible(accessible);
    }

    private void injectFieldValue(Object target, Field field)
            throws ClassNotFoundException, IllegalAccessException {
        if (field.getGenericType().getTypeName().startsWith("java.util.List")) {
            String itemType = field.getGenericType()
                    .getTypeName()
                    .replaceAll("java.util.List<(.+)>", "$1");
            List<Object> beanList = container.getAll(Class.forName(itemType));
            injectBeans(beanList);
            field.set(target, beanList);
        } else {
            Object bean = container.getFirst(field.getType());
            injectBeans(bean);
            field.set(target, bean);
        }
    }

    private void injectBeans(List<Object> targets)
            throws ClassNotFoundException, IllegalAccessException {
        for (Object target : targets) {
            injectBeans(target);
        }
    }

    private void injectBeans(Object target)
            throws ClassNotFoundException, IllegalAccessException {
        for (Field field : target.getClass().getDeclaredFields()) {
            injectBeans(target, field);
        }
    }
}
