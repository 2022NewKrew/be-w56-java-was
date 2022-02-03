package webserver.http.response;

public class Redirect implements ResponseBody<String> {

    private String redirectUrl;

    private Redirect(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public static Redirect from(String redirectUrl) {
        return new Redirect(redirectUrl);
    }

    @Override
    public String getResponseValue() {
        return redirectUrl;
    }
}
