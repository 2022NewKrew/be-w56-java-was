package lib.was.http;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MethodTest {

    @ParameterizedTest
    @ValueSource(strings = {"get", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "CONNECT"})
    void fromString(String method) {
        Optional<Method> result = Method.fromString(method);

        assertTrue(result.isPresent());
        assertEquals(result.get().name().toUpperCase(Locale.ROOT), method.toUpperCase(Locale.ROOT));
    }
}
