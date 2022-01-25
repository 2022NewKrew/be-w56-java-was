package frontcontroller.controller;

import frontcontroller.MyController;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MyRequestDispatcher;

import java.io.IOException;

public class MemberFormController implements MyController {

    @Override
    public void process(MyHttpRequest request, MyHttpResponse response) throws IOException {

        MyRequestDispatcher dispatcher = request.getRequestDispatcher("/user/form.html");
        dispatcher.forward(request, response);
    }
}
