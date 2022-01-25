package Controller;

import model.UserAccount;
import model.UserAccountDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class UserController implements Controller{
    private final Map<String, Function<Map<String, String>, Map<String, Object>>> methodMapper;
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(){
        methodMapper = new HashMap<>();
        userService = new UserService();

        methodMapper.put("GET /form", this::getForm);
        methodMapper.put("POST /form", this::postForm);
        methodMapper.put("GET /login", this::getLogin);
        methodMapper.put("POST /login", this::postLogin);
        methodMapper.put("GET /login_failed", this::getLoginFailed);
    }

    private Map<String, Object> getForm(Map<String, String> model){
        Map<String, Object> result = new HashMap<>();

        result.put("name", "/user/form.html");

        return result;
    }

    private Map<String, Object> postForm(Map<String, String> model){
        Map<String, Object> result = new HashMap<>();
        String userId = model.get("userId");
        String password = model.get("password");
        String name = model.get("name");
        String email = model.get("email");

        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                                            .setUserId(userId)
                                            .setPassword(password)
                                            .setName(name)
                                            .setEmail(email).build();

        try {
            userService.join(userAccountDTO);
        } catch (IllegalStateException e) {
            log.info("아이디 {}로 중복으로 회원 가입을 신청했습니다", userId);
        }

        result.put("name", "redirect:/");

        return result;
    }

    private Map<String, Object> getLogin(Map<String, String> model){
        Map<String, Object> result = new HashMap<>();

        result.put("name", "/user/login.html");

        return result;
    }

    private Map<String, Object> postLogin(Map<String, String> model){
        Map<String, Object> result = new HashMap<>();
        String userId = model.get("userId");
        String password = model.get("password");

        Optional<UserAccount> findUserAccount = userService.findOne(userId);

        if(findUserAccount.isEmpty()){
            log.info("[UserController > login] DB 에서 유저 계정에서 {}로 검색에 실패했습니다.", userId);
            result.put("login", false);
            result.put("name", "redirect:/users/login_failed");
            return result;
        }

        UserAccount userAccount = findUserAccount.get();

        if(!userService.isPasswordEqual(userAccount, password)){
            log.error("[UserController > login] {} 아이디로 로그인 시 입력한 비밀번호가 DB와 일치하지 않습니다.", userAccount.getUserId());
            result.put("login", false);
            result.put("name", "redirect:/users/login_failed");
            return result;
        }

        result.put("sessionedUser", userAccount);
        result.put("login", true);
        result.put("name", "redirect:/");

        return result;
    }

    private Map<String, Object> getLoginFailed(Map<String, String> model){
        Map<String, Object> result = new HashMap<>();

        result.put("name", "/user/login_failed.html");

        return result;
    }

    @Override
    public Function<Map<String, String>, Map<String, Object>> decideMethod(String method, String url) {
        url = method + " " + url.substring(6);

        return methodMapper.get(url);
    }
}
