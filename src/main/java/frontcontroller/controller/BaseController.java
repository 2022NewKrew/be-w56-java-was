package frontcontroller.controller;

import frontcontroller.ModelView;
import frontcontroller.MyController;
import model.User;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MySession;

import java.io.IOException;

public class BaseController implements MyController {

    private final String URL;

    public BaseController(String URL) {
        this.URL = URL;
    }

    @Override
    public ModelView process(MyHttpRequest request, MyHttpResponse response, MySession session) throws IOException {
        User user = (User) session.getAttribute("loginUser");
        if(user != null) {
            System.out.println("loginUser="+user.getName());
        }
        ModelView mv = new ModelView(URL);
        return mv;
    }
}
