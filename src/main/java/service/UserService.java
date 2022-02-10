package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static service.WebService.URL_PREFIX;
import static service.WebService.parseURLBody;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public static User createUser(String testURL){

        HashMap<String, String> parameterMap = parseURLBody(testURL);

        User createUser = new User(parameterMap.get("userId"), parameterMap.get("password"), parameterMap.get("name"), parameterMap.get("email"));
        DataBase.addUser(createUser);

        return createUser;
    }

    public static Boolean loginUser(String urlBody){

        HashMap<String, String> parameterMap = parseURLBody(urlBody);
        User loadUser = DataBase.findUserById(parameterMap.get("userId"));

        if (loadUser==null){
            return false;
        }
        else if (parameterMap.get("password").equals(loadUser.getPassword())){
            return true;
        }
        return false;
    }

    public static String userList(){
        Path path = Path.of(URL_PREFIX + "/user/list.html");
        Collection<User> userCollection = DataBase.findAll();
        try {
            StringBuilder htmlFile = new StringBuilder(Files.readString(path));
            StringBuilder sb = new StringBuilder();

            AtomicInteger index = new AtomicInteger(1);
            userCollection.forEach(user ->
                    sb.append("<tr>\n")
                            .append("<th scope=\"row\">")
                            .append(index.getAndIncrement())
                            .append("</th> <td>")
                            .append(user.getUserId())
                            .append("</td> <td>")
                            .append(user.getName())
                            .append("</td> <td>")
                            .append(user.getEmail())
                            .append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n")
                            .append("</tr>\n"));

            int inputIndex = htmlFile.indexOf("</tbody>");
            htmlFile.insert(inputIndex, sb);

            return htmlFile.toString();

        } catch (Exception e){
            log.debug(e.getMessage());
        }

        return "";
    }

}
