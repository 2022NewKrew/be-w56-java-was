package webserver;

import controller.Controller;
import controller.MemoController;
import controller.StaticController;
import controller.UserController;
import webserver.http.MIME;
import webserver.http.PathInfo;

import java.util.Arrays;

public class ControllerMapper {
    private static final UserController userController = new UserController();
    private static final MemoController memoController = new MemoController();
    private static final StaticController staticController = new StaticController();

    public static Controller mapController(String path) {
        if (path.equals(PathInfo.PATH_INDEX) || path.startsWith("/memo")) {
            return memoController;
        } else if (path.startsWith("/user")) {
            return userController;
        } else if (MIME.isSupportedExtension(path)) {
            return staticController;
        } else {
            throw new IllegalArgumentException("Controller corresponding to path not found");
        }
    }
}
