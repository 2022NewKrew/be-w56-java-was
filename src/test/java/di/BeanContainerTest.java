package di;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BeanContainerTest {

    private BeanContainer subject;

    @BeforeEach
    void setUp() {
        subject = new BeanContainer();
    }

    @Test
    void getFirst_noBeans() {
        subject.put(String.class, new ConstantInstantiator("foo"));

        Object result = subject.getFirst(Integer.class);

        assertNull(result);
    }

    @Test
    void getFirst_singleBean() {
        subject.put(String.class, new ConstantInstantiator("foo"));

        Object result = subject.getFirst(String.class);

        assertEquals("foo", result);
    }

    @Test
    void getFirst_multipleBeans() {
        subject.put(String.class, new ConstantInstantiator("foo"));
        subject.put(int.class, new ConstantInstantiator(1));
        subject.put(int.class, new ConstantInstantiator(2));

        Object result = subject.getFirst(Integer.class);

        assertEquals(1, result);
    }

    @Test
    void getFirst_multipleCalls() throws NoSuchMethodException {
        subject.put(Foo.class, new ClassInstantiator(Foo.class.getDeclaredConstructor()));

        Object result1 = subject.getFirst(Foo.class);
        Object result2 = subject.getFirst(Foo.class);

        assertSame(result1, result2);
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

    public static class Foo {}
}
