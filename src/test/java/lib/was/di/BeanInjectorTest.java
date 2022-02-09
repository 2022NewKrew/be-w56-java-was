package lib.was.di;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class BeanInjectorTest {

    @Test
    void inject() {
        BeanContainer container = new BeanContainer();
        container.put(String.class, new ConstantInstantiator("Hello"));
        container.put(String.class, new ConstantInstantiator("World"));
        container.put(int.class, new ConstantInstantiator(1));
        container.put(Integer.class, new ConstantInstantiator(2));
        container.put(int.class, new ConstantInstantiator(3));
        container.put(boolean.class, new ConstantInstantiator(true));
        BeanInjector subject = new BeanInjector();

        Foo foo = new Foo();
        subject.inject(container, foo);

        assertEquals("Hello", foo.getString());
        assertEquals(List.of(1, 2, 3), foo.getInts());
        assertEquals(true, foo.getBoolean());
    }

    private static class Foo {

        @Inject
        private String string;

        @Inject
        private List<Integer> ints;

        @Inject
        private Boolean booleanValue;

        public String getString() {
            return string;
        }

        public List<Integer> getInts() {
            return ints;
        }

        public Boolean getBoolean() {
            return booleanValue;
        }
    }

    private static class ConstantInstantiator implements Instantiator {

        private final Object value;

        public ConstantInstantiator(Object value) {
            this.value = value;
        }

        @Override
        public Class<?>[] getParameterTypes() {
            return new Class[0];
        }

        @Override
        public Object newInstance(Object[] parameters) throws InstantiationException, IllegalAccessException, InvocationTargetException {
            return value;
        }
    }
}
