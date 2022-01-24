package request;

import util.ResponseData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GetRequestProcess implements RequestProcess{
    private String url;
    private ResponseData responseData;
    public GetRequestProcess(String url) {
        this.url = url;
        if (url.equals("/")) this.url = "/index.html";
    }
    @Override
    public void process() throws IOException {
        String bodyType = "html";
        byte[] body = "wrong url".getBytes();
        if (url.indexOf('.') != -1) {
            bodyType = url.substring(url.lastIndexOf(".") + 1);
            body = getFileResponseBody(url);
        }

        responseData = new ResponseData(getFileResponseHeader(body.length, bodyType), body);
    }

    @Override
    public ResponseData getResponseData() {
        return responseData;
    }

    private String getFileResponseHeader(int lengthOfBodyContent, String bodyType) {
        return "HTTP/1.1 200 OK \r\n"+
                "Content-Type: text/"+bodyType+";charset=utf-8\r\n"+
                "Content-Length: " + lengthOfBodyContent + "\r\n"+
                "\r\n";
    }

    private byte[] getFileResponseBody(String url) throws IOException {
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }
}
