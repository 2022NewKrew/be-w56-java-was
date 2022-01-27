package mapper;

public class ResponseSendDataModel {
    private final String name;
    private Boolean login = null;

    public ResponseSendDataModel(String name){
        this.name = name;
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
