package controllers;

import model.Request;
import model.Response;
import util.IOUtils;

import java.io.IOException;

public class FormController extends Controller {

    @Override
    public void getMethod(Request request, Response response) throws IOException {
        byte[] body = IOUtils.readFromFile(request);
        String extension = IOUtils.getExtension(request);
        response.build200Response(body, extension);
    }

    @Override
    public void postMethod(Request request, Response response) {
    }
}
