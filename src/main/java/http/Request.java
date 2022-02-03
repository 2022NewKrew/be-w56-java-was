package http;

import java.util.Map;

public class Request {
    private final String requestHeader;
    private final String requestBody;
    private final String requestMethod;
    private final String requestUrl;

    private final Map<String, String> parameters;

    public Request(String requestHeader, String requestBody, String requestMethod, String requestUrl,
                   Map<String, String> parameters) {
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
        this.parameters = parameters;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
