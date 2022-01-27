package adaptor.in.web.model;

import infrastructure.model.Path;

public enum RequestPath {

    HOME("/"),
    SIGN_UP("/user/create"),
    LOGIN("/user/login"),
    LOGIN_FAILED("/user/login_failed.html");

    private final String value;

    RequestPath(String path) {
        this.value = path;
    }

    public boolean equalsValue(Path path) {
        return this.value.equals(path.getValue());
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
