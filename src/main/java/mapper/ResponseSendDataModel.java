package mapper;

import model.UserAccount;
import service.SessionService;
import webserver.request.HttpRequest;
import webserver.session.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ResponseSendDataModel {
    private final String name;
    private Optional<Session> login = Optional.empty();
    private final Map<String, Object> dataModel;

    public ResponseSendDataModel(String name, HttpRequest httpRequest){
        this.name = name;
        this.dataModel = new HashMap<>();

        if(httpRequest.getHeader().getCookie().containsKey("id")){
            SessionService sessionService = SessionService.getInstance();

            Optional<Session> session = sessionService.findOne(httpRequest.getHeader().getCookie().get("id"));

            if(session.isPresent())
                dataModel.put("sessionedId", session);
        }
    }

    //테스트용 생성자
    public ResponseSendDataModel(String name){
        this.name = name;
        this.dataModel = new HashMap<>();

        this.login = Optional.empty();
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
