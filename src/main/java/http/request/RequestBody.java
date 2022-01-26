package http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBody {

    Logger logger = LoggerFactory.getLogger(RequestBody.class);

    private final String body;

    public RequestBody(String rawBody) {
        logger.info(rawBody);
        this.body = rawBody;
    }
}
