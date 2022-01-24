package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private String[] requestLine;
    private Map<String, String> requestHeader = new HashMap<>();

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        initializeHttpRequest(br);
    }

    private void initializeHttpRequest(BufferedReader br) throws IOException {
        initializeRequestLine(br);
        initializeRequestHeader(br);
    }

    private void initializeRequestHeader(BufferedReader br) throws IOException {
        String header = br.readLine();
        while (!"".equals(header) && header != null) {
            String headerName = header.substring(0, header.indexOf(":"));
            String headerContent = header.substring(header.indexOf(":") + 2);
            requestHeader.put(headerName, headerContent);
            header = br.readLine();
        }
    }

    private void initializeRequestLine(BufferedReader br) throws IOException {
        requestLine = br.readLine().split(" ");
        if (requestLine == null) {
            throw new IllegalArgumentException("Request Header가 정상적이지 않습니다.");
        }
    }

    public String getUri() {
        return requestLine[1];
    }

    public Optional<String> getHeader(String headerName) {
        return Optional.ofNullable(requestHeader.get(headerName));
    }

    public void loggingRequestHeader() {
        log.info(requestLine[0] + " " + requestLine[1] + " " + requestLine[2]);
        for (String header : requestHeader.keySet()) {
            log.info(header + ": " + requestHeader.get(header));
        }
    }
}
