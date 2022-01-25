package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;


public class Request {

    private final Logger logger = LoggerFactory.getLogger(Request.class);
    private final RequestHeader requestHeader;

    public Request(BufferedReader br) throws IOException {
        this.requestHeader = new RequestHeader(br);
        logger.debug("{}", this.requestHeader);
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }
}
