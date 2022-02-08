package template;

import exception.InternalErrorException;
import handler.result.ModelAndView;
import handler.result.View;
import http.response.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {
    private static final String VIEW_FILE_ROOT_PATH = "./webapp";

    public static ResponseBody resolveStaticFile(String uri) throws IOException {
        String filePath = VIEW_FILE_ROOT_PATH + uri;
        byte[] fileBytes = Files.readAllBytes(new File(filePath).toPath());
        return ResponseBody.of(fileBytes);
    }

    public static ResponseBody resolveModelAndView(ModelAndView modelAndView) {
        String viewFilePath = VIEW_FILE_ROOT_PATH + modelAndView.getViewName();
        byte[] renderedView = TemplateParser.parse(viewFilePath, modelAndView.getModels());
        return ResponseBody.of(renderedView);
    }

    public static ResponseBody resolveView(View view) {
        String viewName = view.getViewName();
        byte[] renderedView;
        try {
            renderedView = Files.readAllBytes(new File(VIEW_FILE_ROOT_PATH + viewName).toPath());
        } catch (IOException e) {
            throw new InternalErrorException(e.getMessage());
        }

        return ResponseBody.of(renderedView);
    }
}
