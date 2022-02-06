package http.request;

import com.google.common.net.HttpHeaders;
import http.common.Headers;
import http.common.HttpHeader;
import http.common.HttpVersion;
import http.request.utils.IOUtils;
import http.request.utils.parser.BodyParser;
import http.request.utils.parser.QueryParser;
import http.request.utils.tokenizer.HeaderLineTokenizer;
import http.request.utils.tokenizer.RequestLineTokenizer;
import http.request.utils.tokenizer.UriTokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RawHttpRequest {
    private final HttpMethod httpMethod;
    private final Uri uri;
    private final Queries queries;
    private final HttpVersion httpVersion;
    private final Headers headers;
    private final RawRequestBody rawRequestBody;

    private RawHttpRequest(HttpMethod httpMethod, Uri uri, Queries queries, HttpVersion httpVersion, Headers headers, RawRequestBody rawRequestBody) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.queries = queries;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.rawRequestBody = rawRequestBody;
    }

    public static RawHttpRequest from(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String requestLine = br.readLine();
        String[] requestLineToken = RequestLineTokenizer.tokenize(requestLine);

        HttpMethod httpMethod = HttpMethod.valueOf(requestLineToken[0]);

        String requestUri = requestLineToken[1];
        String[] requestUriToken = UriTokenizer.tokenize(requestUri);

        Uri uri = new Uri(requestUriToken[0]);
        Queries queries = new Queries(QueryParser.parseQuery(requestUriToken[1]));

        HttpVersion httpVersion = HttpVersion.fromString(requestLineToken[2]);

        Headers headers = Headers.empty();
        String headerString = br.readLine();
        while (headerString != null && !headerString.isEmpty()) {
            String[] headerStringToken = HeaderLineTokenizer.tokenize(headerString);
            HttpHeader httpHeader = new HttpHeader(headerStringToken[0], headerStringToken[1]);
            headers.addHeader(httpHeader);
            headerString = br.readLine();
        }

        RawRequestBody rawRequestBody;
        try {
            int contentLength = Integer.parseInt(headers.getHeader(HttpHeaders.CONTENT_LENGTH));
            rawRequestBody = new RawRequestBody(IOUtils.readData(br, contentLength));
        } catch (NumberFormatException e) {
            rawRequestBody = RawRequestBody.empty();
        }

        return new RawHttpRequest(httpMethod, uri, queries, httpVersion, headers, rawRequestBody);
    }

    public String getHeader(String name) {
        return headers.getHeader(name);
    }

    public HttpRequest parse() {
        RequestBody requestBody = new RequestBody(BodyParser.parseBody(rawRequestBody));
        return new HttpRequest(httpMethod, uri, queries, httpVersion, headers, rawRequestBody, requestBody);
    }

    @Override
    public String toString() {
        return "RawHttpRequest{" +
                "httpMethod=" + httpMethod +
                ", uri=" + uri +
                ", queries=" + queries +
                ", httpVersion=" + httpVersion +
                ", headers=" + headers +
                ", rawRequestBody=" + rawRequestBody +
                '}';
    }
}
