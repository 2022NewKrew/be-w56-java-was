package http;

import enums.HttpMethod;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Map;

public class HttpRequestParser {

    private final HttpRequest httpRequest = new HttpRequest();

    public void parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        parseStartLine(br);
        parseHeader(br);
        parseCookie();
        if (httpRequest.getMethod().equals(HttpMethod.POST)) {
            parseBody(br);
        }
    }

    private void parseStartLine(BufferedReader br) throws IOException {
        String startLine = br.readLine();
        Map<String, String> startLineMap = HttpRequestUtils.parseStartLine(startLine);
        Map<String, String> urlMap = HttpRequestUtils.parseUrl(startLineMap.get("url"));
        if (urlMap.containsKey("query")) {
            httpRequest.setQueryString(urlMap.get("query"));
        }
        httpRequest.setStartLine(startLineMap.get("method"), urlMap.get("url"), startLineMap.get("protocol"));
    }

    private void parseHeader(BufferedReader br) throws IOException {
        String headerSingleLine = " ";
        while (true) {
            headerSingleLine = br.readLine();
            if (headerSingleLine == null || headerSingleLine.equals("")) {
                break;
            }
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerSingleLine);
            httpRequest.getHeader().put(pair.getKey(), pair.getValue());
        }
    }

    private void parseCookie() {
        httpRequest.setCookie(HttpRequestUtils.parseCookies(httpRequest.getHeader().get("Cookie")));
    }

    private void parseBody(BufferedReader br) throws IOException {
        if (!httpRequest.getHeader().containsKey("Content-Length")) {
            return;
        }
        int contentLength = Integer.parseInt(httpRequest.getHeader().get("Content-Length"));
        String bodyString = IOUtils.readData(br, contentLength);
        httpRequest.setBody(HttpRequestUtils.parseQueryString(URLDecoder.decode(bodyString, "UTF-8")));
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }
}
