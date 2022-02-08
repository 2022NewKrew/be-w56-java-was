package webserver.http;

import model.Model;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private final String path;
    private final List<Cookie> cookies;
    private final int statusCode;
    private Model model;

    public Response(String path, int statusCode) {
        this.path = path;
        this.statusCode = statusCode;
        this.cookies = new ArrayList<>();
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public String getPath() {
        return path;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    public String getCookie() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < cookies.size(); i++) {
            sb.append(cookies.get(i).toHeader());
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
