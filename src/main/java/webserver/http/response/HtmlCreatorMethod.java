package webserver.http.response;

import java.io.IOException;

@FunctionalInterface
public interface HtmlCreatorMethod {
    public void create(HttpResponse res) throws IOException;
}
