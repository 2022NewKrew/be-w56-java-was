package bin.jayden.service;

import bin.jayden.model.User;
import bin.jayden.repository.UserRepository;
import bin.jayden.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public String getUserListHtml() throws IOException {
        File file = new File(Constants.RESOURCE_PATH + "/user/list.html");
        byte[] htmlBytes = Files.readAllBytes(file.toPath());
        String htmlString = new String(htmlBytes);
        StringBuilder listHtml = new StringBuilder();
        List<User> users = repository.getUserList();
        int size = users.size();
        Iterator<User> iterator = users.iterator();
        for (int i = 1; i <= size; i++) {
            User user = iterator.next();
            listHtml.append("<tr>");
            listHtml.append("<th scope=\"row\">").append(i).append("</th>");
            listHtml.append("<td>").append(user.getUserId()).append("</td>");
            listHtml.append("<td>").append(user.getName()).append("</td>");
            listHtml.append("<td>").append(user.getEmail()).append("</td>");
            listHtml.append("<td><a class=\"btn btn-success\" href=\"#\" role=\"button\">수정</a></td>");
            listHtml.append("</tr>");
        }
        htmlString = htmlString.replace("{{list}}", listHtml.toString());

        return htmlString;
    }

    public String getUserAddResult(User user) {
        int count = repository.insertUser(user);
        if (count > 0) {
            log.info("new User (userId : {}, name : {})", user.getUserId(), user.getName());
            return "redirect:/";
        } else {
            return "중복된 ID입니다.</br><button onclick=\"history.back()\">뒤로가기</button>\n";
        }
    }

    public User getLoginUser(String userId, String password) {
        return repository.getUser(userId, password);
    }

}
