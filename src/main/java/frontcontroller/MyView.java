package frontcontroller;

import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MyRequestDispatcher;

import java.io.IOException;
import java.util.Map;

public class MyView {

    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(MyHttpRequest request, MyHttpResponse response) throws IOException {
        MyRequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    public void render(Map<String, Object> model, MyHttpRequest request, MyHttpResponse response) throws IOException {
        modelToRequestAttribute(model, request);
        MyRequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    private void modelToRequestAttribute(Map<String, Object> model, MyHttpRequest request) {
        model.forEach((key, value) -> request.setAttribute(key, value));
    }

}
