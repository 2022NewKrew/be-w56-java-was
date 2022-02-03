package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class RequestHeader {

    private static final Logger LOG = LoggerFactory.getLogger(RequestHeader.class);

    private final Map<String, String> headerMap;

    private RequestHeader(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public static RequestHeader of(BufferedReader br) {
        Map<String, String> headerMap = new HashMap<>();
        try {
            String line;
            while (!(line = br.readLine()).equals("")) {
                String[] lineItems = line.split(":");
                String headerKey = lineItems[0].trim();
                String headerValue = lineItems[1].trim();

                headerMap.put(headerKey, headerValue);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return new RequestHeader(headerMap);
    }

    public boolean hasBody() {
        return headerMap.containsKey("Content-Length")
            && Integer.parseInt(headerMap.get("Content-Length")) > 0;
    }
}
