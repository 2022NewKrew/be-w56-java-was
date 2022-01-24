package util;

import webserver.model.HttpRequestHeader;

import java.util.Arrays;

public class HttpRequestHeaderUtils {
    public static HttpRequestHeader parseRequestHeader(String request, String lineString) {
        String[] tokens = request.split(" ");
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(tokens[0], tokens[1], tokens[2]);
        Arrays.stream(lineString.split(System.lineSeparator())).forEach(line -> {
            String[] headers = line.split(": ");
            httpRequestHeader.putHeader(headers[0], headers[1]);
        });
        return httpRequestHeader;
    }
}
