package di;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BeanContainerTest {

    private BeanContainer subject;

    @BeforeEach
    void setUp() {
        subject = new BeanContainer();
    }

    @Test
    void getFirst_none() {
        subject.put(String.class, new ConstantInstantiator("foo"));

        Object result = subject.getFirst(Integer.class);

        assertNull(result);
    }

    @Test
    void getFirst_one() {
        subject.put(String.class, new ConstantInstantiator("foo"));

        Object result = subject.getFirst(String.class);

        assertEquals("foo", result);
    }

    @Test
    void getFirst_multiple() {
        subject.put(String.class, new ConstantInstantiator("foo"));
        subject.put(int.class, new ConstantInstantiator(1));
        subject.put(int.class, new ConstantInstantiator(2));

        Object result = subject.getFirst(Integer.class);

        assertEquals(1, result);
    }

    @Test
    void getAll() {
        subject.put(String.class, new ConstantInstantiator("foo"));
        subject.put(int.class, new ConstantInstantiator(1));
        subject.put(int.class, new ConstantInstantiator(2));

        List<Object> result = subject.getAll(Integer.class);

        assertEquals(2, result.size());
        assertEquals(List.of(1, 2), result);
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
