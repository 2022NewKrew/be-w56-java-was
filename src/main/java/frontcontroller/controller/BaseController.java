package frontcontroller.controller;

import frontcontroller.ModelView;
import frontcontroller.MyController;
import util.MyHttpRequest;
import util.MyHttpResponse;

import java.io.IOException;

public class BaseController implements MyController {

    private final String URL;

    public BaseController(String URL) {
        this.URL = URL;
    }

    @Override
    public ModelView process(MyHttpRequest request, MyHttpResponse response) throws IOException {

        ModelView mv = new ModelView(URL);
        return mv;
    }
}
