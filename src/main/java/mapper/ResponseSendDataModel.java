package mapper;

import java.util.Objects;

public class ResponseSendDataModel {
    private final String name;
    private Boolean login = null;

    public ResponseSendDataModel(String name){
        this.name = name;
    }

    public String makeCookieString(){
        if(!Objects.isNull(login)){
            if(login)
                return "Set-Cookie: logined=true; Path=/";

            return "Set-Cookie: logined=false; Path=/";
        }

        return "";
    }

    public void setLogin(Boolean isLogin){
        login = isLogin;
    }

    public String getName() {
        return name;
    }

    public Boolean getLogin() {
        return login;
    }
}
