package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MyHttpRequest{
    private static final String END_OF_REQUEST_LINE = "";
    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String HEADER_KEY_VALUE_DELIMITER = ": ";
    private static final String HEADER_VALUE_DELIMITER = ",";

    private final String method;
    private final String requestURI;
    private final String version;
    private final Map<String, List<String>> headers;

    public MyHttpRequest(BufferedReader br) throws IOException {
        String[] requestLine = br.readLine().split(REQUEST_LINE_DELIMITER);
        this.method = requestLine[0];
        this.requestURI = requestLine[1];
        this.version = requestLine[2];
        this.headers = initHeaders(br);
    }

    private Map<String, List<String>> initHeaders(BufferedReader br) throws IOException {
        Map<String, List<String>> headers = new HashMap<>();
        String headerField;
        while (!(headerField = br.readLine()).equals(END_OF_REQUEST_LINE)) {
            String key = headerField.split(HEADER_KEY_VALUE_DELIMITER)[0];
            List<String> values = Arrays.stream(headerField.split(HEADER_KEY_VALUE_DELIMITER)[1].split(HEADER_VALUE_DELIMITER))
                    .map(String::trim)
                    .collect(Collectors.toList());
            headers.put(key, values);
        }
        return headers;
    }

    @Override
    public String toString() {
        return "MyHttpRequest{" +
                "method='" + method + '\'' +
                ", requestURI='" + requestURI + '\'' +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                '}';
    }

    public String getRequestURI() {
        return requestURI;
    }
}
