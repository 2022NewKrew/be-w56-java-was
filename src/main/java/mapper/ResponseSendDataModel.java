package mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResponseSendDataModel {
    private final String name;
    private Boolean login = null;
    private final Map<String, Object> dataModel;

    public ResponseSendDataModel(String name){
        this.name = name;
        this.dataModel = new HashMap<>();
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

    public void add(String name, Object data){
        dataModel.put(name, data);
    }

    public Object get(String name){
        return dataModel.get(name);
    }
}
