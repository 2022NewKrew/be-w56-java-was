package lib.was.di;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeanParserTest {

    @Test
    void parse() {
        BeanParser subject = new BeanParser();

        BeanContainer result = subject.parse(new Class[] {Baz.class, Foo.class});

        assertEquals(1, result.getAll(Foo.class).size());
        assertEquals(
                Set.of(1, 2),
                result.getAll(Bar.class)
                        .stream()
                        .map(x -> ((Bar) x).value)
                        .collect(Collectors.toSet())
        );
        assertEquals(
                Set.of(3),
                result.getAll(Baz.class)
                        .stream()
                        .map(x -> ((Baz) x).value)
                        .collect(Collectors.toSet())
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

        @Bean
        public int integer() {
            return 3;
        }
    }

    public static class Bar {

        private final int value;

        public Bar(int value) {
            this.value = value;
        }
    }

    @Bean
    public static class Baz {

        private final int value;

        @Inject
        public Baz(int value) {
            this.value = value;
        }
    }
}
