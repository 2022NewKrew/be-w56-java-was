package http.request;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {

    Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final RequestHeader header;
    private final RequestBody body;

    public HttpRequest(String rawData) {
        List<String> splitRawData = parsingRawData(rawData);
        log.info("{}", splitRawData.size());
        this.header = new RequestHeader(splitRawData.get(0));
        this.body = (splitRawData.size() > 1) ? new RequestBody(splitRawData.get(1)) : null;

    }

    public String getUrl() {
        return header.getUrl();
    }

    private List<String> parsingRawData(String rawData) {
        return List.of(rawData.split("\r\n\r\n"));
    }


}
