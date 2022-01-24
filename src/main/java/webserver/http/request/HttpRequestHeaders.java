package webserver.http.request;

import java.util.Map;

public class HttpRequestHeaders {

    private final Map<String, String> headers;

    public HttpRequestHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    enum Header {
        Host("Host"),
        Connection("Connection"),
        Accept("Accept");

        private final String header;

        Header(String header) {
            this.header = header;
        }

        @Override
        public String toString() {
            return header;
        }
    }
}
