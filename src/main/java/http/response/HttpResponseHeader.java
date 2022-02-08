package http.response;

import java.util.EnumMap;
import java.util.Map;

public class HttpResponseHeader {

    private final Map<HttpResponseHeaderKey, String> headerMap;

    private HttpResponseHeader(ResponseHeaderBuilder builder) {
        headerMap = builder.headerMap;
    }

    public Map<HttpResponseHeaderKey, String> getHeaderMap() {
        return headerMap;
    }

    public static class ResponseHeaderBuilder {

        private final Map<HttpResponseHeaderKey, String> headerMap = new EnumMap<> (
            HttpResponseHeaderKey.class);

        public ResponseHeaderBuilder() {
            headerMap.put(HttpResponseHeaderKey.CONTENT_TYPE, "text/html;charset=utf-8");
        }

        public ResponseHeaderBuilder set(HttpResponseHeaderKey httpResponseHeaderKey, String value) {
            headerMap.put(httpResponseHeaderKey, value);
            return this;
        }

        public HttpResponseHeader build() {
            return new HttpResponseHeader(this);
        }
    }
}
