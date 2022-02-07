package controller;

import java.io.IOException;
import java.util.List;
import model.HttpRequest;
import model.HttpResponse;
import model.HttpStatus;
import model.ResponseFactory;

public class UserCreateController implements Controller {

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
        // TODO - User DB에 저장
        // TODO - index 파일 다운로드 되는 거 해결하기 (Why..?)
        return ResponseFactory.getResponse(request, HttpStatus.FOUND);
    }
}
