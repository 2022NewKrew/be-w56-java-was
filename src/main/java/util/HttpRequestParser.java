package util;

import http.HttpRequest;

import java.io.*;
import java.util.Map;

public class HttpRequestParser {

    private HttpRequest httpRequest = new HttpRequest();

    public void parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String startLine = br.readLine();
        Map<String, String> startLineMap = HttpRequestUtils.parseStartLine(startLine);
        httpRequest.setStartLine(startLineMap.get("method"), startLineMap.get("url"), startLineMap.get("protocol"));

        String headerSingleLine = " ";
        while (true) {
            headerSingleLine = br.readLine();
            if (headerSingleLine == null || headerSingleLine.equals("")) {
                break;
            }
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerSingleLine);
            httpRequest.setHeaderValue(pair.getKey(), pair.getValue());
        }
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }
}
