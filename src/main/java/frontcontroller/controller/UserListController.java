package frontcontroller.controller;

import frontcontroller.ModelView;
import frontcontroller.MyController;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MyHttpStatus;

import java.io.IOException;

public class UserListController implements MyController {

    @Override
    public ModelView process(MyHttpRequest request, MyHttpResponse response) throws IOException {
        if (request.getMethod() == MyHttpStatus.POST) {
//            return post(request, response);
        } else if (request.getMethod() == MyHttpStatus.GET) {
            return get(request, response);
        }
        return null;
    }

    private ModelView get(MyHttpRequest request, MyHttpResponse response) throws IOException {
        ModelView mv = new ModelView("/user/list");
        return mv;
    }

//    private ModelView post(MyHttpRequest request, MyHttpResponse response) throws IOException {
//
//    }
}
