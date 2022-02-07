package webserver.http;

import webapp.exception.UnsupportedHttpMethodException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HttpMethodTest {

    @Test
    @DisplayName("지원하지 않는 Http Method에 대해서 적절한 Exception던지기")
    public void givenUnsupportedHttpMethodAsString_whenConvertingToEnum_thenThrowException() {
        // Given
        String unsupportedHttpMethodAsString = "NON_HTTP_METHOD";

        // When & Then
        assertThrows(UnsupportedHttpMethodException.class,
                     () -> HttpMethod.getHttpMethod(unsupportedHttpMethodAsString));
    }

    @ParameterizedTest
    @MethodSource("getSupportedHttpMethods")
    @DisplayName("지원하는 Http Method에 대한 Enum변환 검증")
    public void givenSupportedHttpMethodAsString_whenConvertingToEnum_thenSuccess(String supportedHttpMethodAsString,
                                                                                  HttpMethod expectedHttpMethod) {
        // Given supportedMethodAsString

        // When
        HttpMethod actualHttpMethod = HttpMethod.getHttpMethod(supportedHttpMethodAsString);

        // Then
        assertEquals(expectedHttpMethod, actualHttpMethod);
    }

    static Stream<Arguments> getSupportedHttpMethods() {
        return Stream.of(
                Arguments.of("GET", HttpMethod.GET),
                Arguments.of("POST", HttpMethod.POST),
                Arguments.of("PUT", HttpMethod.PUT),
                Arguments.of("DELETE", HttpMethod.DELETE)
        );
    }
}
