package template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemplateEngineTest {

    private TemplateEngine subject;

    @BeforeEach
    void setUp() {
        subject = new TemplateEngine();
    }

    @ParameterizedTest
    @MethodSource("provideRenderParameters")
    void render(String template, Map<String, Object> values, String expected) {
        String result = subject.render(template, values);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideRenderParameters() {
        return Stream.of(
                Arguments.of(
                        "x{{item}}y",
                        Map.of("item", 1),
                        "x1y"
                ),
                Arguments.of(
                        "{{#list}}{{foo}}{{bar}}{{/list}}",
                        Map.of(
                                "list", List.of(
                                        new FooContainer(1, "a"),
                                        new FooContainer(2, "b")
                                )
                        ),
                        "1a2b"
                ),
                Arguments.of(
                        "x{{#list1}}y{{foo}}{{#list2}}z{{foo}}{{/list2}}{{/list1}}",
                        Map.of(
                                "list1", List.of(1, 2, 3),
                                "list2", List.of(
                                        new FooContainer(4, "a"),
                                        new FooContainer(5, "b"),
                                        new FooContainer(6, "c")
                                ),
                                "foo", "abc"
                        ),
                        "xyabcz4z5z6yabcz4z5z6yabcz4z5z6"
                ),
                Arguments.of(
                        "{{#list}}{{foo}}{{bar}}{{/list}}",
                        Map.of("list", Collections.emptyList()),
                        ""
                ),
                Arguments.of(
                        "{{#list}}{{x.foo}}{{/list}}",
                        Map.of("list", List.of(new Wrapper<>(new FooContainer(1, "a")))),
                        "1"
                )
        );
    }

    private static class FooContainer {

        private final int foo;
        private final String bar;

        public FooContainer(int foo, String bar) {
            this.foo = foo;
            this.bar = bar;
        }
    }

    private static class Wrapper<T> {

        private final T x;

        public Wrapper(T x) {
            this.x = x;
        }
    }
}
