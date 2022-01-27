package webserver.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;

public class RequestLogger {

    private final Logger logger = LoggerFactory.getLogger(RequestLogger.class);

    void request(HttpRequest request) {
        logger.info("{} {}", request.getMethod(), request.getUri());
    }
}
