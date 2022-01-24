package webserver.handler;

import exception.InvalidRequestException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.StatusCode;

public class GetStaticFileHandler implements Handler{
    private static final String STATIC_FILE_ROOT_PATH = "./webapp";

    @Override
    public void handle(Request request, Response response) throws Exception {
        response.setVersion(request.getVersion());
        response.setStatusCode(StatusCode.OK);

        Path path = new File(STATIC_FILE_ROOT_PATH + request.getUri()).toPath();
        String mimeType = Files.probeContentType(path);
        if (mimeType == null) {
            throw new InvalidRequestException("요청한 파일을 처리할 수 없음");
        }
        response.setContents(mimeType + ";charset=utf-8", Files.readAllBytes(path));
    }
}
