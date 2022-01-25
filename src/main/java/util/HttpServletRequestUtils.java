package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.RequestHandler;
import webserver.request.RequestInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequestUtils {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static RequestInfo parseRequestLine(String requestLine) {
        RequestInfo requestInfo;
        String[] tokens = requestLine.split(" ");
        String[] split = tokens[2].split("\\?", 2);
        if(split.length > 2)
            requestInfo = new RequestInfo(tokens[0],tokens[1],split[0],split[1]);
        else
            requestInfo = new RequestInfo(tokens[0],tokens[1],split[0]);
        return requestInfo;
    }
    public static Map<String,String> readHeader(BufferedReader br) throws IOException {
        Map<String,String> headerMap = new HashMap<>();
        String line;
        while (!(line = br.readLine()).equals("")) {
            String[] split = line.split(":",2);
            headerMap.put(split[0].trim(),split[1].trim());
            log.debug("header = {}", line);
        }
        return headerMap;
    }
}
