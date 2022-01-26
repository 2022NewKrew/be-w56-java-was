package webserver.handler;

import exception.InvalidRequestException;
import java.io.File;
import java.nio.file.Files;
import org.apache.tika.Tika;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.StatusCode;

public class GetStaticFileHandler implements Handler {

    private static final String STATIC_FILE_ROOT_PATH = "./webapp";

    @Override
    public void handle(Request request, Response response) throws Exception {
        response.setVersion(request.getVersion());
        response.setStatusCode(StatusCode.OK);

        File file = new File(STATIC_FILE_ROOT_PATH + request.getUri());
        String mimeType = new Tika().detect(file);
        if (mimeType == null) {
            throw new InvalidRequestException("file " + file + "의 mimeType 이 " + mimeType);
        }
        response.setContents(mimeType, Files.readAllBytes(file.toPath()));
    }
}
