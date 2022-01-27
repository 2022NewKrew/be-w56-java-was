package di;

import annotation.Inject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class BeanInjectorTest {

    @Test
    void inject() {
        BeanContainer container = new BeanContainer();
        container.put(String.class, "Hello");
        container.put(String.class, "World");
        container.put(Integer.class, 1);
        container.put(Integer.class, 2);
        container.put(Integer.class, 3);
        container.put(Boolean.class, true);
        BeanInjector subject = new BeanInjector(mock(Logger.class));

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
}
