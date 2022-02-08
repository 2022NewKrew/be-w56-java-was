package http;

import static util.HttpRequestUtils.*;

import java.util.Map;

public class Request {
    private static final String COOKIE = "Cookie";
    private static final String SEPARATOR = ";";

    private final Map<String, String> header;
    private final String body;
    private final String method;
    private final String url;
    private final Cookie cookie = new Cookie();

    private final Map<String, String> parameters;

    public Request(Map<String, String> header, String body, String method, String url,
                   Map<String, String> parameters) {
        this.header = header;
        this.body = body;
        this.method = method;
        this.url = url;
        this.parameters = parameters;

        parseValues(header.get(COOKIE), SEPARATOR).forEach((k,v)->{
            cookie.set(k,v);
        });
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Cookie getCookie() {
        return cookie;
    }
}
