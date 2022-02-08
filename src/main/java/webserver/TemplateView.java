package webserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface TemplateView {

    String STATIC_FILE_PATH = "./webapp";

    byte[] renderTemplateModel(String viewName, Model model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    byte[] loadStaticFile(String viewName) throws IOException;
}
