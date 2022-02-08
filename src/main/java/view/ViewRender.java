package view;

import http.HttpStatusCode;

import java.io.IOException;

public interface ViewRender extends View{
    byte[] render() throws IOException;
    String getMimeType();
    HttpStatusCode getStatusCode();
}
