package http;

import model.ModelAndView;
import util.IOUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static util.ConstantValues.*;

public enum HttpStatusCode {
    SUCCESS(200, "Ok"){
        @Override
        public void response(DataOutputStream dos, ModelAndView mv) throws IOException {
            String mimeType = IOUtils.readMimeType(ROOT_DIRECTORY + mv.getViewName());
            byte[] body = Files.readAllBytes(new File(ROOT_DIRECTORY + mv.getViewName()).toPath());

            dos.writeBytes("HTTP/1.1 "+ getStatusCode() + " " + getMessage() + "\r\n");
            dos.writeBytes("Content-Type: " + mimeType +";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");

            dos.write(body, 0, body.length);
            dos.flush();
        }
    },
    REDIRECT(302, "Found"){
        @Override
        public void response(DataOutputStream dos, ModelAndView mv) throws IOException {
            mv.setViewName(mv.getViewName().replaceFirst(REDIRECT_COMMAND, ""));
            dos.writeBytes("HTTP/1.1 "+ getStatusCode() + " " + getMessage() + "\r\n");
            dos.writeBytes("Location: "+ ROOT_URL + mv.getViewName() + "\r\n");
            if (mv.getValue("login") != null && mv.getValue("login").equals(true)){
                dos.writeBytes("Set-Cookie: logined=" + mv.getValue("login") + "; Path=/\r\n");
            }
        }
    },
    BAD_REQUEST(400, "Bad Request"){
        @Override
        public void response(DataOutputStream dos, ModelAndView mv) throws IOException {
            dos.writeBytes("HTTP/1.1 "+ getStatusCode() + " " + getMessage() + "\r\n");
        }
    },
    UNAUTHROIZED(401, "Unauthorized"){
        @Override
        public void response(DataOutputStream dos, ModelAndView mv) throws IOException {
            dos.writeBytes("HTTP/1.1 "+ getStatusCode() + " " + getMessage() + "\r\n");
        }
    },
    FORBIDDEN(403, "Forbidden"){
        @Override
        public void response(DataOutputStream dos, ModelAndView mv) throws IOException {
            dos.writeBytes("HTTP/1.1 "+ getStatusCode() + " " + getMessage() + "\r\n");
        }
    },
    NOT_FOUND(404, "Not Found"){
        @Override
        public void response(DataOutputStream dos, ModelAndView mv) throws IOException {
            dos.writeBytes("HTTP/1.1 "+ getStatusCode() + " " + getMessage() + "\r\n");
        }
    };

    private final int statusCode;
    private final String message;

    HttpStatusCode(int statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode(){ return statusCode; }

    public String getMessage() { return message; }

    public abstract void response(DataOutputStream dos, ModelAndView mv) throws IOException;
}
