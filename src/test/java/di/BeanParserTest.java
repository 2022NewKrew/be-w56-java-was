package di;

import annotation.Bean;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeanParserTest {

    @Test
    void parse() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        BeanParser subject = new BeanParser();

        BeanContainer result = subject.parse(new Class[] {Foo.class});

        assertEquals(1, result.getAll(Foo.class).size());
        assertEquals(
                List.of(1, 2),
                result.getAll(Bar.class)
                        .stream()
                        .map(x -> ((Bar) x).value)
                        .collect(Collectors.toList())
        );
    }

    @Bean
    public static class Foo {

        @Bean
        public Bar bar1() {
            return new Bar(1);
        }

        @Bean
        public Bar bar2() {
            return new Bar(2);
        }
    }

    public static class Bar {

        private final int value;

        public Bar(int value) {
            this.value = value;
        }
    }
}
