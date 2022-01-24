package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UrlMapperTest {

    UrlMapper urlMapper;
    @BeforeEach
    void setUp() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        urlMapper = new UrlMapper();
    }

    @Test
    void getTest() throws InvocationTargetException, IllegalAccessException {
        String url = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\n";
        HttpRequest httpRequest = new HttpRequest(url);
        assertThat(urlMapper.control(httpRequest)).isEqualTo("/index.html");
    }
}
