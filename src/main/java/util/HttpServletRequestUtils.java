package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequestUtils {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public RequestInfo parseRequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        RequestInfo requestInfo = new RequestInfo(tokens[0],tokens[1],tokens[2]);
        return requestInfo;
    }
    public Map<String,String> readHeader(BufferedReader br) throws IOException {
        Map<String,String> headerMap = new HashMap<>();
        String line;
        while (!(line = br.readLine()).equals("")) {
            String[] split = line.split(":");
            headerMap.put(split[0].trim(),split[1].trim());
            log.debug("header = {}", line);
        }
        return headerMap;
    }
    public class RequestInfo{
        private String method;
        private String url;
        private String protocol;
        public RequestInfo(String method, String url, String protocol) {
            this.method = method;
            this.url = url;
            this.protocol = protocol;
        }

        public String getMethod() {
            return method;
        }

        public String getUrl() {
            return url;
        }

        public String getProtocol() {
            return protocol;
        }
    }
}
