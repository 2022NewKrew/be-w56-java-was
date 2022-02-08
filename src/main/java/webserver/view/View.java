package webserver.view;

import webserver.http.HttpResponse;
import webserver.model.Model;

import java.io.IOException;

public interface View {
    void render(HttpResponse response, Model model) throws IOException;
}
