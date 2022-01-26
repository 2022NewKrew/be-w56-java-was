package util;

import http.HttpRequest;

import java.io.*;

public class HttpRequestParser {

    private HttpRequest httpRequest = new HttpRequest();

    public void parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String startLine = br.readLine();
        httpRequest.setStartLine(startLine);

        String headerSingleLine = " ";
        while (true) {
            headerSingleLine = br.readLine();
            if (headerSingleLine == null || headerSingleLine.equals("")) {
                break;
            }
            httpRequest.setHeaderValue(headerSingleLine);
        }
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }
}
