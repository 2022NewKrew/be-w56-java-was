package adaptor.in.web;

import adaptor.in.web.exception.FileNotFoundException;
import adaptor.in.web.exception.UriNotFoundException;
import infrastructure.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HomeController {

    private static final HomeController INSTANCE = new HomeController();
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private HomeController() {
    }

    public static HomeController getInstance() {
        return INSTANCE;
    }

    public HttpResponse handle(HttpRequest httpRequest) throws FileNotFoundException, UriNotFoundException {
        Path path = httpRequest.getRequestPath();

        try {
            if (path.matchHandler("")) {
                return home();
            }
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        throw new UriNotFoundException();
    }

    public HttpResponse home() throws IOException {
        return new HttpResponse(
                ResponseLine.valueOf(HttpStatus.OK),
                HttpHeader.of(Pair.of("Content-Type", ContentType.HTML.convertToResponse())),
                HttpByteArrayBody.setFile("/index.html")
        );
    }
}
