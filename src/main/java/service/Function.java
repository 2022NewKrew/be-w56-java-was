package service;

import model.Login;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public enum Function {
    CREATE("create"){
        private String redirectURL;

        @Override
        public byte[] callFunction(HashMap<String, String> parameters, Login login){
            User user = UserService.createUser(parameters.get("body"));
            redirectURL = "/index.html";
            parameters.put("redirectURL", redirectURL);
            return WebService.openUrl(redirectURL);
        }
    },
    LOGIN("login"){
        private String redirectURL;

        @Override
        public byte[] callFunction(HashMap<String, String> parameters, Login login){
            if (UserService.loginUser(parameters.get("body"))){
                log.debug("login success");
                redirectURL = "/index.html";
                login.setLogin(true);
            }
            else{
                log.debug("login failed");
                redirectURL = "/user/login_failed.html";
                login.setLogin(false);
            }
            parameters.put("redirectURL", redirectURL);
            return WebService.openUrl(redirectURL);
        }
    },
    LIST("list.html"){
        @Override
        public byte[] callFunction(HashMap<String, String> parameters, Login login){
            return UserService.userList().getBytes(StandardCharsets.UTF_8);
        }
    },
    NO_FUNCTION(""){
        @Override
        public byte[] callFunction(HashMap<String, String> parameters, Login login){
            return WebService.openUrl(parameters.get("URL"));
        }
    };

    Function(String url) {
        this.url = url;
    }

    private static final Logger log = LoggerFactory.getLogger(Function.class);
    private String url;
    public abstract byte[] callFunction(HashMap<String, String> parameters, Login login);

    public static Function loadFunction(String name){
        for(Function item : Function.values()) {
            if(name.equals(item.url)) {
                return item;
            }
        }
        return Function.NO_FUNCTION;
    }

}
