package webserver;

import java.io.BufferedReader;
import java.io.IOException;

public class MyRequest {

    private static final int METHOD = 0;
    private static final int REQUEST_URI = 1;
    private String method;
    private String requestUri;

    private MyRequest() {
    }

    public MyRequest(BufferedReader httpRequestBufferedReader) throws IOException {
        String request = httpRequestBufferedReader.readLine();
        String[] requestInfo = request.split(" ");

        this.method = requestInfo[METHOD];
        this.requestUri = requestInfo[REQUEST_URI];
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }
}
