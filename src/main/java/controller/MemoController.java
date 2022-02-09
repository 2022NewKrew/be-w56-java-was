package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.MemoService;
import webserver.ResponseGenerator;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.MIME;
import webserver.http.PathInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class MemoController implements Controller{
    private static final Logger log = LoggerFactory.getLogger(MemoController.class);
    private final MemoService memoService = new MemoService();

    @Override
    public HttpResponse controlRequest(HttpRequest httpRequest) {
        String path = httpRequest.getPath();

        if (path.equals(PathInfo.PATH_MEMO_FORM)) {
            if (httpRequest.isLoggedIn()) {
                return ResponseGenerator.generateStaticResponse(PathInfo.PATH_MEMO_FORM_FILE);
            }
            return ResponseGenerator.generateResponse302(PathInfo.PATH_LOGIN_PAGE);
        }
        if (path.equals(PathInfo.PATH_MEMO_CREATE_REQUEST)) {
            try {
                memoService.store(httpRequest);
                return ResponseGenerator.generateResponse302(PathInfo.PATH_INDEX);
            } catch (Exception e) {
                return ResponseGenerator.generateResponse400(e);
            }
        } else if(path.equals(PathInfo.PATH_INDEX)) {
            try {
                return ResponseGenerator.generateIndexResponse(memoService.findAll());
            } catch (Exception e) {
                return ResponseGenerator.generateResponse500();
            }
        } else if (Arrays.stream(MIME.values()).anyMatch(mime -> mime.isExtensionMatch(path))) {
            return ResponseGenerator.generateStaticResponse(path);
        } else {
            log.debug("Page not found");
            return ResponseGenerator.generateResponse404();
        }
    }
}
