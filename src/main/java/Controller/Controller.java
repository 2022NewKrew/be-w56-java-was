package Controller;

import java.io.IOException;
import java.sql.SQLException;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public interface Controller {

    void process(HttpRequest request, HttpResponse response) throws IOException, SQLException, ClassNotFoundException;
}
