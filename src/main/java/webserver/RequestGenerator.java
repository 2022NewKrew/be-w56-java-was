package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.http.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestGenerator {
    private static final Logger log = LoggerFactory.getLogger(RequestGenerator.class);

    public static HttpRequest generateHttpRequest(BufferedReader br) throws IOException{
        String line = br.readLine();
        log.debug("request line: {}", line);
        String method, path, version;
        Map<String, String> headers, parameters;

        String[] tokens = line.split(" ");
        method = tokens[0];
        switch (method) {
            case "GET":
                String[] pathAndParams = tokens[1].split("\\?");
                path = pathAndParams[0];
                if (pathAndParams.length > 1) {
                    parameters = HttpRequestUtils.parseQueryString(pathAndParams[1]);
                } else {
                    parameters = new HashMap<>();
                }
                version = tokens[2];
                headers = getHeaders(br);
                break;

            case "POST":
                path = tokens[1];
                version = tokens[2];
                headers = getHeaders(br);
                String requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                parameters = HttpRequestUtils.parseQueryString(requestBody);
                break;

            default:
                throw new IOException();
        }
        return new HttpRequest(method, path, headers, parameters, version);
    }

    private static Map<String, String> getHeaders(BufferedReader br) throws IOException {
        Map<String, String> result = new HashMap<>();
        String line = br.readLine();
        while(!line.equals("")) {
            log.debug("header: {}", line);
            String[] headerTokens = line.split(": ");
            if (headerTokens.length == 2) {
                result.put(headerTokens[0], headerTokens[1]);
            }
            line = br.readLine();
        }
        return result;
    }
}
