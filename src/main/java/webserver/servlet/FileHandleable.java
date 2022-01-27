package webserver.servlet;

import java.io.File;
import java.io.IOException;
import webserver.http.HttpResponse;

public interface FileHandleable {

    HttpResponse write(HttpResponse response, File file) throws IOException;

}
