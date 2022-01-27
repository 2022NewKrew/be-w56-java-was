package di;

import annotation.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeanInjectorTest {

    @Test
    void inject() throws ClassNotFoundException, IllegalAccessException {
        BeanContainer container = new BeanContainer();
        container.put(String.class, "Hello");
        container.put(String.class, "World");
        container.put(Integer.class, 1);
        container.put(Integer.class, 2);
        container.put(Integer.class, 3);
        container.put(Boolean.class, true);
        BeanInjector subject = new BeanInjector(container);

        Foo foo = new Foo();
        subject.inject(foo);

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
}
