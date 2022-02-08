package util;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.Method;
import http.Request;
import http.Response;
import webserver.ControllerMapper;
import webserver.WebServer;

public final class RequestParser {
    private static final String PARAMETER_SEPARATOR = ":";
    private static final String SPACE = " ";
    private static final String NEW_LINE = "\r\n";

    private static final Logger log = LoggerFactory.getLogger(WebServer.class);

    private RequestParser() {}

    public static Request getRequest(String requestHeader, String requestBody, String method) throws IOException {
        String url = getUrl(requestHeader);
        Map<String, String> parameters = getParameters(method, requestHeader, requestBody);

        Map<String, String> keyValues = new HashMap<>();
        Arrays.stream(requestHeader.split(NEW_LINE)).skip(0).forEach(e -> {
            String[] kv = e.split(PARAMETER_SEPARATOR);
            if (kv.length < 2) {
                return;
            }

            keyValues.put(kv[0].trim(), kv[1].trim());
        });

        return new Request(keyValues, requestBody, method, url, parameters);
    }

    public static String processRequest(Request request, Response response) {
        return ControllerMapper.getController(request.getUrl()).run(request, response);
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
        if (method.equals(Method.GET.toString())) {
            String url = getUrl(requestHeader);
            return HttpRequestUtils.parseQueryString(url);
        }
        return HttpRequestUtils.parseQueryString(requestBody);
    }

    public static Integer getRequestBodyLength(String requestHeader) {
        return Integer.parseInt(requestHeader.split(NEW_LINE)[3].split(PARAMETER_SEPARATOR)[1].trim());
    }
}
