package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Request extends HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private String method;
    private String requestURI;
    private Map<String, String> headers;
    private Map<String, String> params;

    public Request(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String requestLine = br.readLine();

        if(requestLine != null) {
            log.info("request line : {}", requestLine);
            setRequestLine(requestLine);
            setHeader(br);
        }
    }

    private void setRequestLine(String requestLine) {
        String method = requestLine.split(" ")[0];
        String requestURI = requestLine.split(" ")[1].split("\\?")[0];
        String queryString = requestLine.split(" ")[1].split("\\?")[1];

        this.method = method;
        this.requestURI = requestURI;

        Map<String, String> reqParam = HttpRequestUtils.parseQueryString(queryString);
        if (!reqParam.isEmpty())
            this.params = reqParam;
    }

    private void setHeader(BufferedReader br) throws IOException {
        String nextLine;
        Map<String, String> headers = new HashMap<>();
        while (!(nextLine = br.readLine()).equals("")){
            log.info("header : {}", nextLine);
            String[] header = nextLine.split(": ");
            headers.put(header[0], header[1]);
        }
        this.headers = headers;
    }

    public String getContentType() {
        return headers.get("Accept").split(",")[0];
    }

    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public Optional<BodyPublisher> bodyPublisher() {
        return Optional.empty();
    }

    @Override
    public String method() {
        return null;
    }

    @Override
    public Optional<Duration> timeout() {
        return Optional.empty();
    }

    @Override
    public boolean expectContinue() {
        return false;
    }

    @Override
    public URI uri() {
        return URI.create(requestURI);
    }

    @Override
    public Optional<HttpClient.Version> version() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return null;
    }
}
