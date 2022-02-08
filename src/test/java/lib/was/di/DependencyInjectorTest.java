package lib.was.di;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class DependencyInjectorTest {

    @Test
    void inject() {
        DependencyInjector di = new DependencyInjector(mock(Logger.class));
        Stub stub = new Stub();

        di.inject("lib.was.di", stub);

        assertNotNull(stub.value);
        assertNotNull(stub.value.bar);
        assertEquals(
                Set.of("foo", "bar", "baz"),
                stub.value.baz.stream().map(x -> x.value).collect(Collectors.toSet())
        );
    }

    private static class Stub {

        @Inject
        private Foo value;
    }

    @Bean
    public static class Foo {

        @Inject
        private Bar bar;

        @Inject
        private List<Baz> baz;
    }

    @Bean
    public static class Bar {

        @Bean
        public Baz provideBaz1() {
            return new Baz("foo");
        }

        @Bean
        public Baz provideBaz2() {
            return new Baz("bar");
        }

        @Bean
        public Baz provideBaz3() {
            return new Baz("baz");
        }
    }

    public static class Baz {

        private final String value;

        public Baz(String value) {
            this.value = value;
        }
    }
}
