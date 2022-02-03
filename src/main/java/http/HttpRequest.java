package http;

import java.util.List;
import java.util.Map;

public class HttpRequest {

    private final Url url;
    private final HttpVersion version;
    private final Map<HttpHeader, String> headers;
    private final Map<String, String> parameters;

    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.version = builder.version;
        this.headers = builder.headers;
        this.parameters = builder.parameters;
    }

    public HttpResponse respond() {
        return new HttpResponse(version, HttpStatus.OK, headers, null, null);
    }

    public boolean hasAllParameters(List<String> parameters) {
        if (this.parameters.isEmpty()) {
            return false;
        }
        return parameters.stream().allMatch(this.parameters::containsKey);
    }

    public Url getUrl() {
        return url;
    }

    public String getHeader(HttpHeader header) {
        return headers.get(header);
    }

    public String getParameter(String parameter) {
        return parameters.get(parameter);
    }

    public static class Builder {

        private Url url;
        private HttpVersion version;
        private Map<HttpHeader, String> headers;
        private Map<String, String> parameters;

        public Builder url(Url url) {
            this.url = url;
            return this;
        }

        public Builder version(HttpVersion version) {
            this.version = version;
            return this;
        }

        public Builder headers(Map<HttpHeader, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder parameters(Map<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
