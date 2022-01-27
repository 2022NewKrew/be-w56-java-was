package framework.controller.view;

import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.http.enums.HttpStatus;
import framework.http.enums.MediaType;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
public class ViewController {
    private static final Logger log = LoggerFactory.getLogger(ViewController.class);

    private final HttpRequest httpRequest;

    public ViewController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @RequestMapping(value = "/view", method = "GET")
    public HttpResponse getView() throws IOException {
        File file = new File("./webapp" + httpRequest.getPath());
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse(HttpStatus.OK, responseHeader, body);
    }
}
