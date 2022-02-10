package controller;

import db.MemoRepository;
import db.UserRepository;
import domain.*;
import model.Memo;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class PostController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private static final UserRepository userRepository = UserRepository.getInstance();
    private static final MemoRepository memoRepository = MemoRepository.getInstance();

    @Override
    public void control(DataOutputStream dos, BufferedReader bufferedReader, RequestLine requestLine) throws IOException {
        HttpController httpController = init(bufferedReader, requestLine);
        HttpBody httpBody = httpController.getHttpBody(bufferedReader);

        switch (httpController.getRequestPath()) {
            case "/user/create":
                User user = new User(httpBody.get("userId"), httpBody.get("password"), httpBody.get("name"), httpBody.get("email"));
                userRepository.addUser(user);
                log.info("FindUser : {}", userRepository.findUserById(httpBody.get("userId")));

                HttpResponse.response302(dos, "/");
                break;

            case "/qna/form":
                String memoLoginId = httpController.getCookie("loginId");

                Memo memo = new Memo(memoLoginId, httpBody.get("memo"), LocalDate.now());
                memoRepository.addMemo(memo);

                HttpResponse.response302(dos, "/");
                break;

            case "/user/login":
                User findUser = userRepository.findUserById(httpBody.get("userId"));
                String redirectPath = selectLoginRedirectPath(httpBody.get("password"), findUser);
                String logined = selectLoginCookie(httpBody.get("password"), findUser);
                String loginId = selectLoginId(httpBody.get("password"), findUser);

                HttpResponse.response302(dos, redirectPath, logined, loginId);
                break;
        }
    }

    private String selectLoginId(String loginPassword, User user) {
        if (user != null && checkPassword(loginPassword, user.getPassword())) {
            return "loginId=" + user.getUserId() + "; Path=/";
        }
        return "loginId=; Path=/";
    }

    private String selectLoginCookie(String loginPassword, User user) {
        if (user != null && checkPassword(loginPassword, user.getPassword())) {
            return "logined=true; Path=/";
        }
        return "logined=false; Path=/";
    }

    private String selectLoginRedirectPath(String loginPassword, User user) {
        if (user != null && checkPassword(loginPassword, user.getPassword())) {
            return "/";
        }
        return "/user/login_failed";
    }

    private boolean checkPassword(String loginPassword, String storedPassword) {
        return loginPassword.equals(storedPassword);
    }
}
