package app.controller;

import util.http.HttpRequest;
import util.http.HttpRequestUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class RequestBuilder {
    public static HttpRequest makeRequest(String request) throws IOException {
        InputStream is = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return HttpRequestUtils.parseRequest(br);
    }

}
