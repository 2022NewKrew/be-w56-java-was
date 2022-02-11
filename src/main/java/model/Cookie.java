package model;

public class Cookie {

    public static Cookie makeByLoginSuccess(boolean isLogined) {
        return new Cookie(isLogined);
    }

    private final String PATH = "/";
    private final String COOKIE_SEPARATOR = "; ";
    private final boolean isLogined;

    public Cookie(boolean isLogined) {
        this.isLogined = isLogined;
    }

    @Override
    public String toString() {
        return "logined=" + isLogined + COOKIE_SEPARATOR + "Path=" + PATH;
    }
}
