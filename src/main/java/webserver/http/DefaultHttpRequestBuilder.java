package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import webserver.util.HttpRequestUtils;
import webserver.util.HttpRequestUtils.Pair;

public class DefaultHttpRequestBuilder {

    private HttpVersion version;
    private HttpMethod method;
    private String uri;
    private HttpHeader trailingHeaders;
    private HttpRequestParams params;

    public DefaultHttpRequestBuilder init(String requestLine) {
        if (requestLine == null) {
            throw new NullPointerException("requestLine이 존재하지 않습니다.");
        }

        String[] requestLineTokens = requestLine.split(" ");
        this.method = HttpMethod.valueOf(requestLineTokens[0]);
        this.version = new HttpVersion(requestLineTokens[2]);

        String[] uriParamsTokens = requestLineTokens[1].split("\\?");
        this.uri = uriParamsTokens[0];
        if (uriParamsTokens.length > 1) {
            params = new HttpRequestParams(uriParamsTokens[1]);
            params.getParameters().get("userId");
        }
        return this;
    }

    public DefaultHttpRequestBuilder readHeaders(BufferedReader reader) throws IOException {
        trailingHeaders = new HttpHeader();
        String line;
        while (!(line = reader.readLine()).equals("")) {
            Pair headerPair = HttpRequestUtils.parseHeader(line);
            trailingHeaders.set(headerPair.getKey(), headerPair.getValue());
        }
        return this;
    }

    public HttpRequest build() {
        return new HttpRequest(version, method, uri, params, trailingHeaders);
    }
}
