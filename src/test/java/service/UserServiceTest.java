package service;

import model.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Test
    public void UserCreate(){
        UserService webService = new UserService();

        String testURL = "userId=aa&password=bb&name=cc&email=dd%40gg";
        User user = new User("aa", "bb", "cc", "dd%40gg");

        User createdUser = UserService.createUser(testURL);

        log.debug("createdUser {}", createdUser.toString());

        assert(user.getUserId().equals(createdUser.getUserId()));
        assert(user.getEmail().equals(createdUser.getEmail()));
        assert(user.getName().equals(createdUser.getName()));
        assert(user.getPassword().equals(createdUser.getPassword()));

    }

    @Test
    public void openURLTest(){
        String line = "/index.html";
        byte[] openFile =  WebService.openUrl(line);
        log.debug("openFile {}", openFile);
        assert(openFile != null);
    }

    @Test
    public void loginUserSuccessTest(){
        String line = "userId=aa&password=bb";
        String signupLine = "userId=aa&password=bb&name=cc&email=dd%40gg";

        User createdUser = UserService.createUser(signupLine);
        Boolean loginResult = UserService.loginUser(line);

        assert (loginResult.equals(true));
    }

    @Test
    public void loginUserFailTest(){
        String line = "userId=kk&password=dd";
        String signupLine = "userId=aa&password=bb&name=cc&email=dd%40gg";

        User createdUser = UserService.createUser(signupLine);
        log.debug("createdUser {}", createdUser.toString());
        Boolean loginResult = UserService.loginUser(line);

        assert (loginResult.equals(false));
    }

    @Test
    public void userList() {
        String signupLine = "userId=aa&password=bb&name=cc&email=dd%40gg";
        String loginLine = "userId=aa&password=bb";
        String compareList =
                "                <tr>\n" +
                "                    <th scope=\"row\">1</th> <td>aa</td> <td>cc</td> <td>dd%40gg</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                "                </tr>\n";
        UserService.createUser(signupLine);
        Boolean loginResult = UserService.loginUser(loginLine);

        String resultLine = UserService.userList();
        log.debug("userList results {}", resultLine);

        assert (resultLine.equals(compareList));
    }
}
