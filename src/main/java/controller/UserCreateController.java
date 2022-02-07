package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import model.HttpRequest;
import model.HttpResponse;
import model.HttpStatus;
import model.Mime;

public class UserCreateController implements Controller {

    private static final byte[] NOT_FOUNT_MESSAGE = "없는 페이지 입니다.".getBytes();
    private static UserCreateController instance;

    private static String parseExtension(String path) {
        List<String> splitResult = List.of(path.split("\\."));
        int length = splitResult.size();
        return splitResult.get(length - 1);
    }

    public static synchronized UserCreateController getInstance() {
        if (instance == null) {
            instance = new UserCreateController();
        }
        return instance;
    }

    @Override
    public HttpResponse run(HttpRequest request) throws IOException {
        File file = new File("./webapp/index.html");
        if (!file.exists()) {
            return HttpResponse.of(HttpStatus.NOT_FOUND, NOT_FOUNT_MESSAGE, Mime.getMime(""));
        }
        return HttpResponse.of(HttpStatus.OK, Files.readAllBytes(file.toPath()), parseExtension(request.getPath()));
    }
}
