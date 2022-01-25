package frontcontroller.controller;

import frontcontroller.MyController;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MyRequestDispatcher;

import java.io.IOException;

public class BaseController implements MyController {

    private final String URL;

    public BaseController(String URL) {
        this.URL = URL;
    }

    @Override
    public void process(MyHttpRequest request, MyHttpResponse response) throws IOException {
        MyRequestDispatcher dispatcher = request.getRequestDispatcher(URL);
        dispatcher.forward(request, response);
    }
}
