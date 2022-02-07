package servlet;

public class ViewResolver {
    private static final String STATIC_FILE_PREFIX = "./webapp";
    private static final String TEMPLATE_FILE_PREFIX = "./webapp/template";
    private static final String HOST_URL_PREFIX = "http://localhost:8080";

    private ViewResolver() {
    }

    public static View findView(ServletResponse response) {
        String path = response.getPath();

        if (response.isRedirect()) {
            String removeRedirectPrefix = path.substring(path.indexOf(":") + 1);
            return new RedirectView(HOST_URL_PREFIX + removeRedirectPrefix, response.getCookie());
        }

        if (response.isStatic()) {
            return new StaticView(STATIC_FILE_PREFIX + path);
        }

        if (response.isModelView()) {
            return new ModelView(TEMPLATE_FILE_PREFIX + path, response.getModel());
        }

        // TODO throw exception
        return new StaticView(STATIC_FILE_PREFIX + path);
    }
}
