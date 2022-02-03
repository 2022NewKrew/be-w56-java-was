package webserver.framwork.core.view;

import webserver.framwork.http.response.HttpResponse;

public interface View {
    String getContentType();

    void render(HttpResponse response);
}
