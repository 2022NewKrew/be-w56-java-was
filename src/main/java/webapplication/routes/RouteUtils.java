package webapplication.routes;

public class RouteUtils {

    public static String makeKey(String action, String uri) {
        return action + " " + uri;
    }
}
