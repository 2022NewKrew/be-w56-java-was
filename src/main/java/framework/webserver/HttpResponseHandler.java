package framework.webserver;

import framework.util.Cookies;
import framework.util.exception.InternalServerException;
import framework.view.ModelView;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

import static framework.util.Constants.DEFAULT_CONNECTION;
import static framework.util.Constants.DEFAULT_HTTP_VERSION;

/**
 * Client에게 응답할 정보를 담은 클래스
 */
@Getter
public class HttpResponseHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpResponseHandler.class);

    private final DataOutputStream dos;
    private final Cookies cookies = new Cookies();

    @Setter
    private String httpVersion = DEFAULT_HTTP_VERSION;

    @Setter
    private String connection = DEFAULT_CONNECTION;

    public HttpResponseHandler(DataOutputStream dos) {
        this.dos = dos;
    }

    public void flush(ModelView modelView) {
        try {
            dos.writeBytes(httpVersion + " " + modelView.getStatusCode() + " "
                    + modelView.getStatusCodeString() + "\r\n");
            if (modelView.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                dos.writeBytes("Location: " + modelView.getUri() + "\r\n");
            }
            dos.writeBytes("Content-Length: " + modelView.getContentLength() + "\r\n");
            dos.writeBytes("Connection: " + connection + "\r\n");
            dos.writeBytes("Content-Type: " + modelView.getContentType() +  "\r\n");

            if (!cookies.isEmpty()) {
                dos.writeBytes("Set-Cookie: " + cookies);
            }

            dos.writeBytes("\r\n");
            dos.write(modelView.getContent(), 0, modelView.getContentLength());
            dos.flush();
        } catch (IOException e) {
            throw new InternalServerException();
        }
    }

    public void setCookie(String key, String value) {
        cookies.setCookie(key, value);
    }

    public void setCookie(String key, String value, String path) {
        cookies.setCookie(key, value, path);
    }

    public String getCookie(String key) {
        return cookies.getCookie(key);
    }
}
