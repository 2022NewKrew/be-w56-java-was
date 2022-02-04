package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.manage.RequestParser;
import webserver.response.format.ForwardResponseFormat;
import webserver.response.format.ResponseFormat;
import webserver.response.ResponseCode;

import java.io.*;

public class GetController implements MethodController {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    RequestParser rp;
    OutputStream os;

    public GetController(RequestParser rp, OutputStream os) {
        this.rp = rp;
        this.os = os;
    }

    public void service() {
        log.info(":: GET Service");

        switch (rp.getPath()) {
            default:
                methodDefault();
                break;
        }
    }

    private void methodDefault () {
        ResponseFormat rf = new ForwardResponseFormat(os, rp.getPath());
        rf.sendResponse(ResponseCode.STATUS_200);
    }
}
