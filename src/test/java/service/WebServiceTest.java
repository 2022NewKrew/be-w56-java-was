package service;

import model.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServiceTest {

    private static final Logger log = LoggerFactory.getLogger(WebService.class);


    @Test
    public void UserCreate(){
        WebService webService = new WebService();

        String testURL = "userId=aa&password=bb&name=cc&email=dd%40gg";
        User user = new User("aa", "bb", "cc", "dd%40gg");

        User createdUser = webService.createUser(testURL);

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

        User createdUser = WebService.createUser(signupLine);
        Boolean loginResult = WebService.loginUser(line);

        assert (loginResult.equals(true));
    }

    @Test
    public void loginUserFailTest(){
        String line = "userId=kk&password=dd";
        String signupLine = "userId=aa&password=bb&name=cc&email=dd%40gg";

        User createdUser = WebService.createUser(signupLine);
        log.debug("createdUser {}", createdUser.toString());
        Boolean loginResult = WebService.loginUser(line);

        assert (loginResult.equals(false));
    }


}
