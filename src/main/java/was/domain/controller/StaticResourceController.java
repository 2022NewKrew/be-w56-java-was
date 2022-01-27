package was.domain.controller;

import di.annotation.Bean;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.meta.HttpStatus;
import was.util.StaticResourceReader;

import java.io.IOException;

@Bean
public class StaticResourceController implements Controller {

    private final StaticResourceReader staticResourceReader;

    public StaticResourceController(StaticResourceReader staticResourceReader) {
        this.staticResourceReader = staticResourceReader;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res) throws IOException {

        res.setViewPath(req.getPath());

        res.setStatus(HttpStatus.OK);
        res.setBody(staticResourceReader.read(req.getPath()));
    }
}
