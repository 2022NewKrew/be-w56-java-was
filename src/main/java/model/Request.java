package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;


public class Request {

    private final Logger logger = LoggerFactory.getLogger(Request.class);
    private final RequestHeader requestHeader;
    private RequestBody requestBody = null;

    public Request(BufferedReader br) throws IOException {
        this.requestHeader = new RequestHeader(br);
        logger.debug("{}", this.requestHeader);
        if (!this.requestHeader.getRequestLine().getHttpMethod().equals("GET")) {
            this.requestBody = new RequestBody(this.requestHeader, br);
        }
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }
}
