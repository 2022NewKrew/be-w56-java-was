package model;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.BadRequestFormatException;
import exceptions.InvalidHttpMethodException;
import exceptions.InvalidHttpVersionException;
import exceptions.InvalidQueryFormatException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class StartLineTest {

    @DisplayName("[성공] startLine 객체를 생성한다")
    @ParameterizedTest(name = "startLineMessage = {0}")
    @ValueSource(strings = {"GET /index.html HTTP/1.1", "   GET /index.html HTTP/1.1   "})
    void of(String startLineMessage) {

        StartLine startLine = StartLine.of(startLineMessage);
    }

    @DisplayName("[실패] 인자 개수가 3개가 아닌 경우")
    @ParameterizedTest(name = "startLineMessage = {0}")
    @ValueSource(strings = {"/index.html HTTP/1.1", "GET POST /index.html HTTP/1.1", "GET"})
    void of_FailedBy_WrongNumberOfToken(String startLineMessage) {
        Assertions.assertThrows(BadRequestFormatException.class,
                () -> StartLine.of(startLineMessage));
    }

    @DisplayName("[실패] 없는 http 버젼을 작성한 경우")
    @ParameterizedTest(name = "startLineMessage = {0}")
    @ValueSource(strings = {"GET /index.html HTTP/2.0", "GET /index.html HTTP/666"})
    void of_FailedBy_InvalidHttpVersion(String startLineMessage) {
        Assertions.assertThrows(InvalidHttpVersionException.class,
                () -> StartLine.of(startLineMessage));
    }

    @DisplayName("[실패] 없는 http 메소드을 작성한 경우")
    @ParameterizedTest(name = "method = {0}")
    @ValueSource(strings = {"AAA /index.html HTTP/1.1", "GETT /index.html HTTP/1.1"})
    void of_FailedBy_InvalidHttpMethod(String startLineMessage) {
        Assertions.assertThrows(InvalidHttpMethodException.class,
                () -> StartLine.of(startLineMessage));
    }

    @Test
    @DisplayName("[실패] startLine이 빈 문자열인 경우")
    void of_FailedBy_EmptyString() {
        String startLineMessage = "";

        Assertions.assertThrows(BadRequestFormatException.class,
                () -> StartLine.of(startLineMessage));
    }

    @Test
    @DisplayName("[성공] method를 잘 들고와야 한다")
    void getHttpMethod() {
        String startLineMessage = HttpMethod.GET.name() + " /index.html HTTP/1.1";
        StartLine startLine = StartLine.of(startLineMessage);

        HttpMethod methodResult = startLine.getHttpMethod();

        Assertions.assertEquals(methodResult.name(), HttpMethod.GET.name());
    }

    @Test
    @DisplayName("[성공] url을 잘 들고와야 한다")
    void getUrl() {
        String startLineMessage = "GET /index.html HTTP/1.1";
        StartLine startLine = StartLine.of(startLineMessage);

        String urlResult = startLine.getUrl();

        Assertions.assertNotEquals(urlResult, "/index.htm");
        Assertions.assertNotEquals(urlResult, "index.html");
        Assertions.assertEquals(urlResult, "/index.html");
    }

    @Test
    @DisplayName("[성공] url을 잘 들고와야 한다 - 쿼리가 있는 경우")
    void getUrl_By_Query() {
        String startLineMessage = "GET /user/create?aa=bb HTTP/1.1";
        StartLine startLine = StartLine.of(startLineMessage);

        String urlResult = startLine.getUrl();

        Assertions.assertNotEquals(urlResult, "user/create");
        Assertions.assertNotEquals(urlResult, "/user/create?");
        Assertions.assertEquals(urlResult, "/user/create");
    }

    @Test
    @DisplayName("[성공] 쿼리를 잘 들고 와야 한다")
    void getQuery() {
        String startLineMessage = "GET /user/create?aa=bb&cc=dd HTTP/1.1";
        Map<String, String> query = Map.of("aa", "bb", "cc", "dd");
        StartLine startLine = StartLine.of(startLineMessage);

        Map<String, String> queryResult = startLine.getQuery();

        Assertions.assertEquals(queryResult, query);
    }

    @DisplayName("[실패] 쿼리 형식이 잘못 된 경우")
    @ParameterizedTest(name = "query = {0}")
    @ValueSource(strings = {"GET /user/create?aa HTTP/1.1", "GET /user/create?aa=bb&cc HTTP/1.1"})
    void getQuery_FailedBy_Query(String startLineMessage) {

        Assertions.assertThrows(InvalidQueryFormatException.class,
                () -> StartLine.of(startLineMessage));
    }
}
