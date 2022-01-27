package webserver.view;

import webserver.model.HttpResponse;

import java.io.IOException;

public interface View {
    void render(HttpResponse response) throws IOException;
}
