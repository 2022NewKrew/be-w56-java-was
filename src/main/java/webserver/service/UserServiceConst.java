package webserver.service;

public enum UserServiceConst {
    ATTRIBUTE_USER_ID("userId"),
    ATTRIBUTE_PASSWORD("password"),
    ATTRIBUTE_NAME("name"),
    ATTRIBUTE_EMAIL("email"),
    COOKIE_LOGIN("logined");

    public final String key;

    UserServiceConst(String key) {
        this.key = key;
    }
}
