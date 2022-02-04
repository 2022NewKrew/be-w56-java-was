package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.Map;

public class RequestController {

    private static final Logger log = LoggerFactory.getLogger(RequestController.class);
    private static final DataBase database = new DataBase();

    public static void uriMatcher(String methodType, String uri, Map<String, String> requestBody) {
        if (methodType.equals("GET") && uri.equals("/user/create")) {
            postUserByGetRequest(uri);
        }
        else if (methodType.equals("POST") && uri.equals("/user/create")) {
            postUserByPostRequest(uri, requestBody);
        }
    }

    private static void postUserByGetRequest(String uri) {
        String[] parsedUri = uri.split("\\?");
        String path = parsedUri[0];
        String queryString = parsedUri[1];
        Map<String, String> argsContainer = HttpRequestUtils.parseQueryString(queryString);
        User userEntity = new User(argsContainer.get("userId"), argsContainer.get("password"), argsContainer.get("name"), argsContainer.get("email"));
        database.addUser(userEntity);
        log.debug("USER CREATED: " + userEntity.toString());
    }

    private static void postUserByPostRequest(String uri, Map<String, String> requestBody) {
        User userEntity = new User(requestBody.get("userId"), requestBody.get("password"), requestBody.get("name"), requestBody.get("email"));
        database.addUser(userEntity);
        log.debug("USER CREATED: " + userEntity.toString());
    }

}
