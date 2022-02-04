package framework.controller.css;

import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.http.enums.MediaType;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.response.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Controller
public class CssController {
    private static final Logger log = LoggerFactory.getLogger(CssController.class);

    @RequestMapping(value = "/css", method = "GET")
    public HttpResponse getCss(HttpRequest httpRequest) throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_CSS);

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());
    }
}
