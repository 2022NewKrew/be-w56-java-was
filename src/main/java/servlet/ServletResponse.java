package servlet;

import http.header.Cookie;
import http.message.ResponseMessage;
import servlet.view.Model;
import servlet.view.View;

import java.io.IOException;

public class ServletResponse {
    private final Model model;
    private Object path;
    private Cookie cookie;

    private ServletResponse(Object path, Model model, Cookie cookie) {
        this.path = path;
        this.model = model;
        this.cookie = cookie;
    }

    public ServletResponse() {
        this(null, new Model(), null);
    }

    public boolean isEmpty() {
        return path == null;
    }

    public Model getModel() {
        return model;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public void setPath(Object path) {
        this.path = path;
    }

    public ResponseMessage createResponseMessage() throws IOException {
        View view = ResponseType.createView(path, model.isEmpty());
        if (view == null) {
            throw new NullPointerException();
        }
        return view.render(model, cookie);
    }
}
