package util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class MyHttpResponse {

    private static final String PRE_FIX = "./webapp";
    private String redirectURI;
    private DataOutputStream dos;
    private String accept;
    private String viewPath;
    private byte[] body;
    private MyHttpResponseStatus status;

    private MyHttpResponse() {
    }

    public MyHttpResponse(final OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(String viewPath) throws IOException {
        this.body = Files.readAllBytes(new File(PRE_FIX + viewPath).toPath());
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public MyHttpResponseStatus getStatus() {
        return status;
    }

    public void setStatus(MyHttpResponseStatus status) {
        this.status = status;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
