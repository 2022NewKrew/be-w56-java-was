package webserver.response;

import exception.CustomException;
import exception.InvalidRequestException;
import java.io.File;
import org.apache.tika.Tika;

public final class ResponseFactory {

    public static StaticFileResponse staticFileResponse(String path) throws Exception {
        File file = new File(path);
        String mimeType = new Tika().detect(file);
        if (mimeType == null) {
            throw new InvalidRequestException("file " + file + ": 지원하지 않는 타입");
        }
        return new StaticFileResponse(mimeType, file);
    }

    public static RedirectResponse redirectResponse(String uri) {
        return new RedirectResponse(uri);
    }

    public static ErrorResponse errorResponse(Exception e) {
        if (e instanceof CustomException) {
            return new ErrorResponse(((CustomException) e).getStatusCode(), e.getMessage());
        }
        return new ErrorResponse(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
