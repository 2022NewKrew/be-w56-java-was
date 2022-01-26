package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.RequestHandler;
import webserver.request.RequestStartLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequestUtils {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static RequestStartLine parseRequestLine(String startLine) {
        RequestStartLine requestStartLine;
        String[] tokens = startLine.split(" ");
        String[] split = tokens[1].split("\\?", 2);
        if(split.length == 2)
            requestStartLine = new RequestStartLine(tokens[0],tokens[2],split[0],split[1]);
        else
            requestStartLine = new RequestStartLine(tokens[0],tokens[2],split[0]);
        return requestStartLine;
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
