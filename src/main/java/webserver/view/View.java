package webserver.view;

import webserver.http.HttpResponse;

import java.io.IOException;

public interface View {
    void render(HttpResponse response) throws IOException;
}
