package Controllers;

import db.DataBase;
import model.Request;
import model.Response;
import model.User;
import org.h2.engine.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Map;

public class UserController extends Controller{

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private String url;

    @Override
    public void getMethod(Request request, Response response) throws IOException {
        this.url = request.getRequestHeader().getRequestLine().getUrl();

        String path = url.substring(5);
        if(path.startsWith("/create")){
            joinController(request, response);
        }
    }

    private void joinController(Request request, Response response) throws IOException {
        String queryString = this.url.split("\\?")[1];
        Map query = HttpRequestUtils.parseQueryString(queryString);
        User joiningUser = new User(query);
        DataBase.addUser(joiningUser);
        logger.info("User(userId = {}) joined.", joiningUser.getUserId());

        response.buildBody("/index.html");
        response.buildResponse();
    }

    @Override
    public void postMethod(Request request, Response response) {
    }
}
