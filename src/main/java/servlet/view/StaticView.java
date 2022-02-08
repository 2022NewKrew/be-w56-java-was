package servlet.view;

import http.header.Cookie;
import http.header.MimeType;
import http.message.ResponseMessage;
import http.startline.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticView implements View {
    private final String path;

    public StaticView(String path) {
        this.path = "./webapp" + path;
    }

    @Override
    public ResponseMessage render(Model model, Cookie cookie) throws IOException {
        byte[] bytes = Files.readAllBytes(new File(path).toPath());
        return ResponseMessage.create(HttpStatus.OK, MimeType.matchOf(path), bytes, cookie);
    }
}
