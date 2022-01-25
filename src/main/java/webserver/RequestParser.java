package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(RequestParser.class);

    public RequestMap parse(BufferedReader br) throws IOException {
        RequestMap requestMap = new RequestMap();
        parsingHeader(br, requestMap);
        return requestMap;
    }

    private void parsingHeader(BufferedReader br, RequestMap requestMap) throws IOException {
        String line = br.readLine();
        log.info("request line {}: ", line);
        String[] requestLine = line.split(" ");
        requestMap.addHeader("url", requestLine[1]);

        while (!line.equals("")) {
            line = br.readLine();
            log.debug("request header {}: ", line);
        }
    }
}
