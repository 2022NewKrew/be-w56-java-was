package mapper;

import model.UserAccount;
import webserver.session.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ResponseSendDataModel {
    private final String name;
    private Optional<Session> login = Optional.empty();
    private final Map<String, Object> dataModel;

    public ResponseSendDataModel(String name){
        this.name = name;
        this.dataModel = new HashMap<>();
    }

    public String makeCookieString(){
        return login.map(session -> "Set-Cookie: id=" + session.getSessionId() + "; max-age:" + session.getMaxAge() + "; Path=/").orElse("");
    }

    public void setLogin(Optional<Session> userAccount){
        login = userAccount;
    }

    public String getName() {
        return name;
    }

    public void add(String name, Object data){
        dataModel.put(name, data);
    }

    public Object get(String name){
        return dataModel.get(name);
    }
}
