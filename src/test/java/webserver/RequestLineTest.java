package webserver;

import org.junit.jupiter.api.Test;
import webserver.context.HttpMethod;
import webserver.context.RequestLine;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestLineTest {
    @Test
    public void create_method() {
        RequestLine line = new RequestLine("GET /index.html HTTP/1.1");
        assertEquals(HttpMethod.GET, line.getMethod());
        assertEquals("/index.html", line.getPath());

        line = new RequestLine("POST /index.html HTTP/1.1");
        assertEquals("/index.html", line.getPath());
    }

    @Test
    public void create_path_and_params() {
        RequestLine line = new RequestLine("GET /user/create?uesrId=javajigi&password=pass HTTP/1.1");
        assertEquals(HttpMethod.GET, line.getMethod());
        assertEquals("/user/create", line.getPath());
        Map<String, String> params = line.getParams();
        assertEquals(2, params.size());
    }

}
