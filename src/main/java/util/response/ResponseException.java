package util.response;

import util.HtmlBuilder;
import util.HttpStatus;
import util.Message;
import util.Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ResponseException {
    public static Response notFoundResponse() throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/404.html").toPath());
        return new ResponseBuilder()
                .setHttpStatus(HttpStatus.NOT_FOUND)
                .addHeader("Content-Type","text/html;charset=utf-8")
                .addHeader("Content-length", String.valueOf(body.length))
                .setBody(body)
                .build();
    }

    public static Response errorResponse(Message message, HttpStatus httpStatus) throws IOException {
        Model model = new Model();
        List<Message> messageList = new ArrayList<>();
        messageList.add(message);
        model.setAttribute("error",messageList);
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        String html = htmlBuilder.build("./webapp/error.html", model);
        byte[] body = html.getBytes();
        return new ResponseBuilder()
                .setHttpStatus(httpStatus)
                .addHeader("Content-Type","text/html;charset=utf-8")
                .addHeader("Content-length", String.valueOf(body.length))
                .setBody(body)
                .build();
    }
}
