package was.domain.requestHandler;

import di.annotation.Bean;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.util.StaticResourceReader;

import java.io.IOException;

@Bean
public class StaticViewResolverHandler implements RequestHandler {

    private final StaticResourceReader staticResourceReader;

    public StaticViewResolverHandler(StaticResourceReader staticResourceReader) {
        this.staticResourceReader = staticResourceReader;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res) throws IOException {
        if (res.hasNotViewPath()) {
            return;
        }

        final byte[] body = staticResourceReader.read(res.getViewPath());
        res.setBody(body);
    }
}
