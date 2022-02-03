package webserver.model;

import com.google.common.collect.Maps;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class WebHttpRequest extends HttpRequest {
    private final RequestMethod method;
    private final String requestURI;
    private final String version;
    private final Map<String, String> cookies;
    private final Map<String, List<String>> headers;
    private final String body;

    private WebHttpRequest(BufferedReader in) throws IOException {
        String[] request = in.readLine().split(" ");
        this.method = RequestMethod.valueOf(request[0]);
        this.requestURI = request[1];
        this.version = request[2];
        headers = new HashMap<>();
        String lines = in.lines()
                .takeWhile(line -> !line.equals(""))
                .collect(Collectors.joining(System.lineSeparator()));
        List<HttpRequestUtils.Pair> headers = Arrays.stream(lines.split(System.lineSeparator()))
                .map(HttpRequestUtils::parseHeader)
                .collect(Collectors.toList());
        cookies = headers.stream()
                .filter(header -> header.getKey().equals("Cookie"))
                .findFirst()
                .map(HttpRequestUtils.Pair::getValue)
                .map(HttpRequestUtils::parseCookies)
                .orElse(Maps.newHashMap());
        headers.stream()
                .filter(header -> !header.getKey().equals("Cookie"))
                .forEach(header -> {
                    List<String> values = Arrays.stream(header.getValue().split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());
                    this.headers.put(header.getKey(), values);
                });
        if (method == RequestMethod.POST && this.headers.containsKey("Content-Length")) {
            this.body = IOUtils.readData(in, Integer.parseInt((String) this.headers.get("Content-Length").get(0)));
        } else {
            this.body = "";
        }
    }

    public static WebHttpRequest of(BufferedReader in) throws IOException {
        return new WebHttpRequest(in);
    }

    public List<String> getCookie() {
        return headers.get("Cookie");
    }

    public String getBody() {
        return body;
    }

    @Override
    public Optional<BodyPublisher> bodyPublisher() {
        // TODO: Request body 구현
        return Optional.empty();
    }

    @Override
    public String method() {
        return method.toString();
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
        if (version.equals("HTTP/1.1")) {
            return Optional.of(HttpClient.Version.HTTP_1_1);
        }
        if (version.equals("HTTP/2.0")) {
            return Optional.of(HttpClient.Version.HTTP_2);
        }
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return HttpHeaders.of(headers, (k, v) -> true);
    }

    @Override
    public String toString() {
        return "WebHttpRequest{" +
                "method='" + method + '\'' +
                ", requestURI='" + requestURI + '\'' +
                ", version='" + version + '\'' +
                ", headers=" + headers.toString() +
                '}';
    }
}
