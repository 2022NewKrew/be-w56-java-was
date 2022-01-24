package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String method;
    private final String requestURI;
    private final String version;
    private final Map<String, String> headers;

    private HttpRequest(BufferedReader in) throws IOException {
        String[] requestHeaderParams = in.readLine().split(" ");
        this.method = requestHeaderParams[0];
        this.requestURI = requestHeaderParams[1];
        this.version = requestHeaderParams[2];
        this.headers = new HashMap<>();
        String inputLine;
        while (!(inputLine = in.readLine()).equals("")) {
            String[] inputs = inputLine.split(": ");
            headers.put(inputs[0], inputs[1]);
        }
    }

    public static HttpRequest of(BufferedReader in) throws IOException {
        return new HttpRequest(in);
    }

    public String getMethod() {
        return method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
