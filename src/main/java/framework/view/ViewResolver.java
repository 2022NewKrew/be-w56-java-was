package framework.view;

public class ViewResolver {
    public static void resolve(ModelView modelView) throws Exception {
        // Redirect & Static Files
        if (modelView.isStatic()) {
            modelView.readStaticFile();
        }

        // TODO: Forward
    }
}
