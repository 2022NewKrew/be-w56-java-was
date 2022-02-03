package util;

import static util.IOUtils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import http.Request;
import http.Response;
import webserver.ControllerMapper;

public final class RequestParser {
    private static final String PARAMETER_SEPARATOR = "\\?";
    private static final String SPACE = " ";
    private static final String NEW_LINE = "\r\n";
    private static final String EMPTY = "";

    private static final String GET_METHOD = "GET";

    private RequestParser() {}

    public static Request getRequest(BufferedReader bufferedReader) throws IOException {
        String requestHeader = getRequestHeader(bufferedReader);
        String requestBody = getRequestBody(bufferedReader, requestHeader);
        String method = getMethod(requestHeader);
        String url = getUrl(requestHeader);
        Map<String, String> parameters = getParameters(method, requestHeader, requestBody);

        return new Request(requestHeader, requestBody, method, url, parameters);
    }

    private static String getRequestHeader(BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while (!(line = bufferedReader.readLine()).equals(EMPTY)) {
            stringBuilder.append(line);
            stringBuilder.append(NEW_LINE);
        }
        return stringBuilder.toString();
    }

    private static String getRequestBody(BufferedReader bufferedReader, String requestHeader) throws IOException {
        if (getMethod(requestHeader).equals(GET_METHOD)) {
            return null;
        }
        Integer bodyLength = getRequestBodyLength(requestHeader);
        return readData(bufferedReader, bodyLength);
    }

    public static String processRequest(Request request, Response response) {
        return ControllerMapper.getController(request.getRequestUrl()).run(request, response);
    }

    public static String getMethod(String requestHeader) {
        return requestHeader.split(SPACE)[0];
    }

    private static String getUrl(String requestHeader) {
        return requestHeader.
                split(SPACE)[1].
                split(NEW_LINE)[0].
                split(PARAMETER_SEPARATOR)[0];
    }

    private static Map<String, String> getParameters(String method, String requestHeader, String requestBody) {
        if (method.equals(GET_METHOD)) {
            String url = getUrl(requestHeader);
            return HttpRequestUtils.parseQueryString(url);
        }
        return HttpRequestUtils.parseQueryString(requestBody);
    }

    private static Integer getRequestBodyLength(String requestHeader) {
        return Integer.parseInt(requestHeader.split(NEW_LINE)[3].split(PARAMETER_SEPARATOR)[1].trim());
    }
}
