package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import webserver.util.HttpRequestUtils;
import webserver.util.HttpRequestUtils.Pair;

public class DefaultHttpRequestBuilder {

    private final BufferedReader reader;
    private HttpVersion version;
    private HttpMethod method;
    private String uri;
    private HttpHeader trailingHeaders;
    private HttpRequestParams params;

    public DefaultHttpRequestBuilder(BufferedReader reader) {
        this.reader = reader;
    }

    private void initRequestLine() throws IOException {
        String requestLine = reader.readLine();
        String[] requestLineTokens = requestLine.split(" ");
        this.method = HttpMethod.valueOf(requestLineTokens[0]);
        this.version = new HttpVersion(requestLineTokens[2]);

        String[] uriParamsTokens = requestLineTokens[1].split("\\?");
        this.uri = uriParamsTokens[0];
        if (uriParamsTokens.length > 1) {
            params = new HttpRequestParams(uriParamsTokens[1]);
            params.getParameters().get("userId");
        }
    }

    private void initHeaders() throws IOException {
        trailingHeaders = new HttpHeader();
        String line;
        while (!(line = reader.readLine()).equals("")) {
            Pair headerPair = HttpRequestUtils.parseHeader(line);
            trailingHeaders.set(headerPair.getKey(), headerPair.getValue());
        }
    }

    public HttpRequest build() throws IOException {
        initRequestLine();
        initHeaders();
        return new HttpRequest(version, method, uri, params, trailingHeaders);
    }
}
