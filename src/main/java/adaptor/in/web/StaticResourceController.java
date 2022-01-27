package adaptor.in.web;

import adaptor.in.web.exception.FileNotFoundException;
import infrastructure.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StaticResourceController {

    private static final Logger log = LoggerFactory.getLogger(StaticResourceController.class);

    public HttpResponse handleFileRequest(HttpRequest httpRequest) {
        Path path = httpRequest.getRequestPath();
        log.debug("Static Resource Request: {}", path);

        try {
            return new HttpResponse(
                    ResponseLine.valueOf(HttpStatus.OK),
                    HttpHeader.of(Pair.of("Content-Type", httpRequest.getRequestPath().getContentType().convertToResponse())),
                    HttpByteArrayBody.setFile(path.getValue())
            );
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }
}
