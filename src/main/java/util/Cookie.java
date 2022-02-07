package util;

public class Cookie {
    private final String id = null;
    private Boolean login = false;

    public Cookie(String cookie) {
        setCookie(cookie);
    }

    private void setCookie(String cookie) {
        if (cookie == null) {
            return;
        }
        String[] tokens = cookie.split(";\\s");
        if (tokens[2].equals("logined=true")) {
            login = true;
        }
    }

    public String getId() {
        return id;
    }

    public Boolean getLogin() {
        return login;
    }
}
