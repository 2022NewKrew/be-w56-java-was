package http.response;

import java.util.EnumMap;
import java.util.Map;

public class HttpResponseHeader {

    private final Map<HttpResponseHeaderEnum, String> headerMap;

    private HttpResponseHeader(ResponseHeaderBuilder builder) {
        headerMap = builder.headerMap;
    }

    public Map<HttpResponseHeaderEnum, String> getHeaderMap() {
        return headerMap;
    }

    public static class ResponseHeaderBuilder {

        private final Map<HttpResponseHeaderEnum, String> headerMap = new EnumMap<> (
            HttpResponseHeaderEnum.class);

        public ResponseHeaderBuilder() {
            headerMap.put(HttpResponseHeaderEnum.CONTENT_TYPE, "text/html;charset=utf-8");
        }

        public ResponseHeaderBuilder set(HttpResponseHeaderEnum httpResponseHeaderEnum, String value) {
            headerMap.put(httpResponseHeaderEnum, value);
            return this;
        }

        public HttpResponseHeader build() {
            return new HttpResponseHeader(this);
        }
    }
}
