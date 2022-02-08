package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class HttpRequestHeader {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestHeader.class);
    private static final String NEW_LINE = System.lineSeparator();
    private static final String HEADER_SEPARATOR = ":";

    private final Map<String, String> headerMap;

    private HttpRequestHeader(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public static HttpRequestHeader of(BufferedReader br) {
        Map<String, String> headerMap = new HashMap<>();
        try {
            String line;
            while (!(line = br.readLine()).equals("")) {
                String[] lineItems = line.split(HEADER_SEPARATOR);
                String headerKey = lineItems[0].trim();
                String headerValue = lineItems[1].trim();

                headerMap.put(headerKey, headerValue);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return new HttpRequestHeader(headerMap);
    }

    public Integer getContentLength() {
        String contentLength = headerMap.get(HttpRequestHeaderKey.CONTENT_LENGTH.getText());
        return contentLength == null ? 0 : Integer.parseInt(contentLength);
    }

    @Override
    public String toString() {
        boolean isFirstLine = true;
        StringBuilder builder = new StringBuilder(NEW_LINE + "Request Header - ");
        for (Map.Entry<String, String> entry: headerMap.entrySet()) {
            String prefix = isFirstLine ? "" : "               - ";
            builder.append(String.format("%s%s: %s", prefix, entry.getKey(), entry.getValue()));
            isFirstLine = false;
        }
        builder.append(NEW_LINE);
        return builder.toString();
    }
}
