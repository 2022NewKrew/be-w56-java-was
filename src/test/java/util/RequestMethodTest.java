package util;

import exception.UnsupportedMethodException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RequestMethodTest {

    @Test
    @DisplayName("지원하지 않는 Http Method에 대해서 적절한 Exception던지기")
    public void givenUnsupportedMethodString_whenConvertingToEnum_thenThrowException() {
        // Given
        String unsupportedMethodAsString = "NOT HTTP METHOD";

        // When & Then
        assertThrows(UnsupportedMethodException.class, () -> RequestMethod.getRequestMethod(unsupportedMethodAsString));
    }

    @ParameterizedTest
    @MethodSource("getSupportedMethods")
    @DisplayName("지원하는 Http Method에 대한 Enum변환 검증")
    public void givenSupportedMethodString_whenConvertingToEnum_thenSuccess(String supportedMethodAsString, RequestMethod expectedRequestMethod) {
        // Given supportedMethodAsString

        // When
        RequestMethod actualRequestMethod = RequestMethod.getRequestMethod(supportedMethodAsString);

        // Then
        assertEquals(expectedRequestMethod, actualRequestMethod);
    }

    static Stream<Arguments> getSupportedMethods() {
        return Stream.of(
                Arguments.of("GET", RequestMethod.GET),
                Arguments.of("POST", RequestMethod.POST),
                Arguments.of("PUT", RequestMethod.PUT),
                Arguments.of("DELETE", RequestMethod.DELETE)
        );
    }
}
