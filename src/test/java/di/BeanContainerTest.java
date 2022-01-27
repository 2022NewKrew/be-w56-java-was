package di;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        subject.put(String.class, "foo");

        Object result = subject.getFirst(Integer.class);

        assertNull(result);
    }

    @Test
    void getFirst_one() {
        subject.put(String.class, "foo");

        Object result = subject.getFirst(String.class);

        assertEquals("foo", result);
    }

    @Test
    void getFirst_multiple() {
        subject.put(String.class, "foo");
        subject.put(Integer.class, 1);
        subject.put(Integer.class, 2);

        Object result = subject.getFirst(Integer.class);

        assertEquals(1, result);
    }

    @Test
    void getAll() {
        subject.put(String.class, "foo");
        subject.put(Integer.class, 1);
        subject.put(Integer.class, 2);

        List<Object> result = subject.getAll(Integer.class);

        assertEquals(2, result.size());
        assertEquals(List.of(1, 2), result);
    }
}
