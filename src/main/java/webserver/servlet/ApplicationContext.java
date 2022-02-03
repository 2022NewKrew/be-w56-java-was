package webserver.servlet;

import app.AppConfig;
import app.RootController;
import app.user.adapter.in.SignUpController;
import java.util.HashMap;

public class ApplicationContext {

    private final HashMap<String, HttpControllable> map;

    public ApplicationContext() {
        AppConfig appConfig = new AppConfig();
        map = new HashMap<>();
        map.put(RootController.path, appConfig.rootController());
        map.put(SignUpController.path, appConfig.signUpController());
    }

    public static ApplicationContext getInstance() {
        return ApplicationContextHolder.INSTANCE;
    }

    public HttpControllable getHandler(String uri) {
        String startSlashRemoved = uri.startsWith("/") ? uri.substring(1) : uri;
        String parentPath = "/" + startSlashRemoved.split("/")[0];

        if (!map.containsKey(parentPath)) {
            throw new NullPointerException("Handler not found. path: " + parentPath);
        }
        return map.get(parentPath);
    }

    private static class ApplicationContextHolder {

        public static ApplicationContext INSTANCE = new ApplicationContext();
    }
}
