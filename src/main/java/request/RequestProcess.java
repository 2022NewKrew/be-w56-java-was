package request;

import util.ResponseData;

import java.io.IOException;

public interface RequestProcess {
    void process() throws IOException;
    ResponseData getResponseData();
}
