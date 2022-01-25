package http;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MethodTest {

    @ParameterizedTest
    @ValueSource(strings = {"get", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "CONNECT"})
    void fromString(String method) {
        Method result = Method.fromString(method);

        assertNotNull(result);
        assertEquals(result.name().toUpperCase(Locale.ROOT), method.toUpperCase(Locale.ROOT));
    }
}
