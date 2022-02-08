package was.http.domain.service.view;

import was.http.meta.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ResponseEntity<T> {
    private final HttpStatus httpStatus;
    private final T body;
    private final Map<String, String> header;

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public ResponseEntity(HttpStatus httpStatus, T body, Map<String, String> header) {
        this.httpStatus = httpStatus;
        this.body = body;
        this.header = header;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Object getBody() {
        return body;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public static class Builder<T> {
        private HttpStatus httpStatus = HttpStatus.OK;
        private T body = null;
        private final Map<String, String> header = new HashMap<>();

        public Builder<T> status(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder<T> body(T body) {
            this.body = body;
            return this;
        }

        public Builder<T> addAllHeader(Map<String, String> header) {
            this.header.putAll(header);
            return this;
        }

        public Builder<T> addHeader(String key, String value) {
            this.header.put(key, value);
            return this;
        }

        public ResponseEntity<T> build() {
            return new ResponseEntity<>(httpStatus, body, header);
        }
    }
}
