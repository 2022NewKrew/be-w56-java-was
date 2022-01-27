package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestBody {
    private final Logger logger = LoggerFactory.getLogger(RequestBody.class);
    private final String body;

    public RequestBody(RequestHeader requestHeader, BufferedReader br) throws IOException {
        Integer contentLength = Integer.parseInt(requestHeader.getRequestHeader().get("Content-Length").trim());
        this.body = IOUtils.readData(br, contentLength);
        logger.debug("requestBody : {}", this.body);
    }

    public String getBody() {
        return body;
    }
}
