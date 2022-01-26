package Controllers;

import model.Request;
import model.Response;

import java.io.IOException;

public class FormController extends Controller {

    @Override
    public void getMethod(Request request, Response response) throws IOException {
        response.buildBody(request);
        response.build200Response();
    }

    @Override
    public void postMethod(Request request, Response response) {
    }
}
