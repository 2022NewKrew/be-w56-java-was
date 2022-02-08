package servlet.view;

import http.message.ResponseMessage;
import http.startline.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticView implements View {
    private final String path;

    public StaticView(String path) {
        this.path = path;
    }

    @Override
    public ResponseMessage render() {
        try {
            byte[] bytes = Files.readAllBytes(new File(path).toPath());
            return ResponseMessage.create(HttpStatus.OK, bytes);
        } catch (IOException e) {
            return ResponseMessage.create(HttpStatus.NOT_FOUND, new byte[]{});
        }
    }
}
