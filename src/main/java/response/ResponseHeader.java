package response;

import java.util.EnumMap;
import java.util.Map;

public class ResponseHeader {

    private final Map<ResHeader, String> headerMap;

    private ResponseHeader(ResponseHeaderBuilder builder) {
        headerMap = builder.headerMap;
    }

    public Map<ResHeader, String> getHeaderMap() {
        return headerMap;
    }

    public static class ResponseHeaderBuilder {

        private final Map<ResHeader, String> headerMap;

        public ResponseHeaderBuilder() {
            headerMap = new EnumMap<> (ResHeader.class);
            headerMap.put(ResHeader.CONTENT_TYPE, "text/html;charset=utf-8");
        }

        public ResponseHeaderBuilder set(ResHeader resHeader, String value) {
            headerMap.put(resHeader, value);
            return this;
        }

        public ResponseHeader build() {
            return new ResponseHeader(this);
        }
    }
}
