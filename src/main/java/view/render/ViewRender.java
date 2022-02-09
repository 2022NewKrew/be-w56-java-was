package view.render;

import http.HttpStatusCode;
import view.View;

import java.io.IOException;

public interface ViewRender extends View {
    byte[] render() throws IOException;
    String getMimeType();
    HttpStatusCode getStatusCode();
}
