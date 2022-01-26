package http.request;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {

    Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final RequestHeader header;
    private final RequestBody body;

    public HttpRequest(String rawData) {
        List<String> splitRawData = parsingRawData(rawData);
        this.header = new RequestHeader(splitRawData.get(0));
        this.body = (splitRawData.size() > 1) ? new RequestBody(splitRawData.get(1)) : null;
    }

    private List<String> parsingRawData(String rawData) {
        return List.of(rawData.split("\r\n\r\n"));
    }

    public String getUrl() {
        return header.getUrl();
    }

    public Map<String, String> getQuery() {
        return header.getQuery();
    }
}
