package util;

import http.HttpRequest;

import java.io.*;
import java.util.Map;

public class HttpRequestParser {

    private HttpRequest httpRequest = new HttpRequest();

    public void parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        parseStartLine(br);
        parseHeader(br);
        if (httpRequest.getMethod().equals("POST")) {
            parseBody(br);
        }
    }

    private void parseStartLine(BufferedReader br) throws IOException {
        String startLine = br.readLine();
        Map<String, String> startLineMap = HttpRequestUtils.parseStartLine(startLine);
        httpRequest.setStartLine(startLineMap.get("method"), startLineMap.get("url"), startLineMap.get("protocol"));
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

    private void parseBody(BufferedReader br) throws IOException {
        if (!httpRequest.getHeader().containsKey("Content-Length")) {
            return;
        }
        int contentLength = Integer.parseInt(httpRequest.getHeader().get("Content-Length"));
        String bodyString = IOUtils.readData(br, contentLength);
        httpRequest.setBody(HttpRequestUtils.parseQueryString(bodyString));
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }
}
