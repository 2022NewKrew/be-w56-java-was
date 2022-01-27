package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.*;

public class Request extends HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private String method;
    private String requestURI;
    private Map<String, List<String>> headers;
    private Map<String, String> params;
    private String body;

    public Request(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String requestLine = br.readLine();

        if (requestLine != null) {
            setRequestLine(requestLine);
            setHeaderAndBody(br);
        }
    }

    private void setRequestLine(String requestLine) {
        String method = requestLine.split(" ")[0];
        String requestURI = requestLine.split(" ")[1];

        if (requestURI.contains("?")) {
            String[] tokens = requestURI.split("\\?");
            requestURI = tokens[0];
            String queryString = tokens[1];
            Map<String, String> reqParam = HttpRequestUtils.parseQueryString(queryString);
            if (!reqParam.isEmpty())
                this.params = reqParam;
        }

        this.method = method;
        this.requestURI = requestURI;

    }

    private void setHeaderAndBody(BufferedReader br) throws IOException {
        String nextLine = "";
        Map<String, List<String>> headers = new HashMap<>();
        while (!(nextLine = br.readLine()).equals("")) {
            String[] header = nextLine.split(": ");
            headers.put(header[0], Arrays.asList(header[1].split(",")));
        }
        this.headers = headers;
        if (method.equals("POST")) {
            this.body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length").get(0)));
        }
    }

    public String getContentType() {
        return headers.get("Accept").get(0);
    }

    public String getBody() {
        return body;
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
        return method;
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
        log.info("String path = request.uri().getPath(); " + requestURI);
        return URI.create(requestURI);
    }

    @Override
    public Optional<HttpClient.Version> version() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return HttpHeaders.of(headers, (s1, s2) -> true);
    }
}
